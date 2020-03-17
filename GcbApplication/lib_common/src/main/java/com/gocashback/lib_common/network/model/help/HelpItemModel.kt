package com.gocashback.lib_common.network.model.help

/**
 * @Author 55HAITAO
 * @Date 2019-06-12 09:33
 */
class HelpItemModel {
    var id = ""//int	分类id
    var name = ""//string	分类名称
    var help: List<HelpChildItemModel>? = null   // array	帮助问题子类

}