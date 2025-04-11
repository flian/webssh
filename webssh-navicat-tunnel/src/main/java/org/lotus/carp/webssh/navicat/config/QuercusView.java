package org.lotus.carp.webssh.navicat.config;

import javax.servlet.*;
import javax.servlet.http.*;

import com.caucho.quercus.*;
import com.caucho.quercus.env.*;

import com.caucho.quercus.page.*;

import com.caucho.quercus.servlet.api.*;
import com.caucho.util.L10N;
import com.caucho.vfs.*;

import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.exception.WebSshBusinessException;
import org.lotus.carp.webssh.quercus.mongodb.wrapper.MongoExtension;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
public class QuercusView extends AbstractUrlBasedView {
    private static final L10N L = new L10N(QuercusView.class);



    protected QuercusContext _quercus;
    protected ServletContext _servletContext;
    protected QuercusServletContext quercusServletContext;


    public QuercusView() {
        super();
    }


    protected void initServletContext(ServletContext servletContext) {
        _servletContext = servletContext;
        quercusServletContext = new QuercusServletContextImpl(_servletContext);
        checkServletAPIVersion();
        if (this._quercus == null) {
            this._quercus = new QuercusContext();
            //this._quercus.setIni("extension","/usr/local/opt/mongodb@8.4/mongodb.so");
        }
        getQuercus().setPwd(new FilePath(_servletContext.getRealPath("/")));
        /*Path phpIniPath = this.getQuercus().getPwd().lookup("/Users/cwnuzj/codes/github/webssh/webssh-navicat-tunnel/src/main/resources/php.ini");
        _quercus.setIniFile(phpIniPath);*/
        //FIXME mongo extend?
        MongoExtension mongoExtension = new MongoExtension(getQuercus());
        getQuercus().addInitModule(mongoExtension);
        //this.getQuercus().setIni("extension","/usr/local/opt/mongodb@8.4/mongodb.so");
        getQuercus().init();

        getQuercus().start();
    }

    protected void checkServletAPIVersion() {
        int major = _servletContext.getMajorVersion();
        int minor = _servletContext.getMinorVersion();

        if (major < 2 || major == 2 && minor < 4)
            throw new QuercusRuntimeException(L.l("Quercus requires Servlet API 2.4+."));
    }

    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Env env = null;
        WriteStream ws = null;
        QuercusHttpServletRequest _request = new QuercusHttpServletRequestImpl(request);
        QuercusHttpServletResponse _response = new QuercusHttpServletResponseImpl(response);
        try {
            QuercusPage page;
            try {
                page = getQuercus().parse(Vfs.openRead(phpScriptStream(request)));
            } catch (FileNotFoundException ex) {
                // php/2001
                log.error(ex.toString(), ex);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);

                return;
            }

            StreamImpl out;

            try {
                out = new VfsStream(null, response.getOutputStream());
            } catch (IllegalStateException e) {
                WriterStreamImpl writer = new WriterStreamImpl();
                writer.setWriter(response.getWriter());

                out = writer;
            }

            ws = new WriteStream(out);

            ws.setNewlineString("\n");

            QuercusContext quercus = getQuercus();
            quercus.setServletContext(quercusServletContext);

            env = quercus.createEnv(page, ws, _request, _response);

            //load mongoExtension const
            MongoExtension.loadConst(env);

            // retro... thanks, Spring
            for (Object entryObj : model.entrySet()) {
                Map.Entry entry = (Map.Entry) entryObj;
                env.setScriptGlobal((String) entry.getKey(), entry.getValue());
            }

            try {
                env.start();

                env.setScriptGlobal("request", request);
                env.setScriptGlobal("response", response);
                env.setScriptGlobal("servletContext", _servletContext);


                StringValue prepend
                        = quercus.getIniValue("auto_prepend_file").toStringValue(env);
                if (prepend.length() > 0) {
                    Path prependPath = env.lookup(prepend);

                    if (prependPath == null)
                        env.error(L.l("auto_prepend_file '{0}' not found.", prepend));
                    else {
                        QuercusPage prependPage = getQuercus().parse(prependPath);
                        prependPage.executeTop(env);
                    }
                }
                env.executeTop();
                StringValue append
                        = quercus.getIniValue("auto_append_file").toStringValue(env);
                if (append.length() > 0) {
                    Path appendPath = env.lookup(append);

                    if (appendPath == null)
                        env.error(L.l("auto_append_file '{0}' not found.", append));
                    else {
                        QuercusPage appendPage = getQuercus().parse(appendPath);
                        appendPage.executeTop(env);
                    }
                }
                //   return;
            } catch (QuercusExitException e) {
                throw e;
            } catch (QuercusErrorException e) {
                throw e;
            } catch (QuercusLineRuntimeException e) {
                log.error(e.toString(), e);

                //  return;
            } catch (QuercusValueException e) {
                log.error(e.toString(), e);

                ws.println(e.toString());

                //  return;
            } catch (Throwable e) {
                if (response.isCommitted())
                    e.printStackTrace(ws.getPrintWriter());

                ws = null;

                throw e;
            } finally {
                if (env != null)
                    env.close();

                // don't want a flush for a thrown exception
                if (ws != null)
                    ws.close();
            }
        } catch (QuercusDieException e) {
            // normal exit
            log.error(e.toString(), e);
        } catch (QuercusExitException e) {
            // normal exit
            log.error(e.toString(), e);
        } catch (QuercusErrorException e) {
            // error exit
            log.error(e.toString(), e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new ServletException(e);
        }
    }

    InputStream phpScriptStream(HttpServletRequest req) {
        String scriptPath = getUrl();
        if (!StringUtils.isEmpty(scriptPath) && scriptPath.contains(WebSshNavicatTunnelConst.WEB_SSH_NAVICAT_PHP_FOLDER)) {
            InputStream result = QuercusView.class.getClassLoader().getResourceAsStream(scriptPath);
            if (null != result) {
                return result;
            }
        }
        throw new WebSshBusinessException("can't find php file.");
    }


    /**
     * Returns the Quercus instance.
     */
    protected QuercusContext getQuercus() {
        return this._quercus;
    }

    /**
     * Gets the script manager.
     */
    public void destroy() {
        _quercus.close();
    }

}