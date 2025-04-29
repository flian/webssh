<?php
import org.lotus.carp.webssh.quercus.mongodb.wrapper.utils.QuercusUtils;
phpinfo();


if(isset($_SESSION['currentMonUrl'])){
    echo 'connect mongoDB URL:' . $_SESSION['currentMonUrl'];
}

QuercusUtils::printValues("log print test ok.");
?>