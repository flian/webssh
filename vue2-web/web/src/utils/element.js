// 导入自己需要的组件
import {
    Form,
    FormItem,
    Dialog,
    Row,
    Col,
    Button,
    ButtonGroup,
    Table,
    TableColumn,
    Input,
    Message,
    Container,
    Header,
    Main,
    Upload,
    Dropdown,
    DropdownMenu,
    DropdownItem,
    Tabs,
    TabPane,
    Divider,
    Tooltip, Select, Option, Checkbox, Tag
} from 'element-ui'
import VueClipboard from 'vue-clipboard2'


const element = {
    install: function (Vue) {
        Vue.use(Input)
        Vue.use(Tag)
        Vue.use(Dialog)
        Vue.use(Row)
        Vue.use(Col)
        Vue.use(Form)
        Vue.use(FormItem)
        Vue.use(Button)
        Vue.use(ButtonGroup)
        Vue.use(Table)
        Vue.use(TableColumn)
        Vue.use(Container)
        Vue.use(Header)
        Vue.use(Main)
        Vue.use(Upload)
        Vue.use(Dropdown)
        Vue.use(DropdownItem)
        Vue.use(DropdownMenu)
        Vue.use(Tabs)
        Vue.use(TabPane)
        Vue.use(Divider)
        Vue.use(Tooltip)
        Vue.use(Select)
        Vue.use(Option)
        Vue.use(Checkbox)
        Vue.use(VueClipboard)
        Vue.prototype.$message = Message
    }
}
export default element
