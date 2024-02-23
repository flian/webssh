package org.lotus.carp.webssh.config.exception;

/**
 * @Author:
 * @Date: 2020/2/7 16:18
 */
public class WebSshBusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Integer code;

    public WebSshBusinessException() {
    }

    public WebSshBusinessException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public WebSshBusinessException(String message) {
        super(message);
    }

    public WebSshBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof WebSshBusinessException)) {
            return false;
        } else {
            WebSshBusinessException other = (WebSshBusinessException)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                Object this$code = this.getCode();
                Object other$code = other.getCode();
                if (this$code == null) {
                    if (other$code != null) {
                        return false;
                    }
                } else if (!this$code.equals(other$code)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof WebSshBusinessException;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        Object $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "BusinessExecption(code=" + this.getMessage() + ")";
    }
}

