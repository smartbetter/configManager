/**
 * navbar.js MIT License 2018/01/12
 *
 * USE:
 * Html:
 * <div class="layui-side layui-bg-black kit-side">
 *     <div class="layui-side-scroll">
 *         <ul id="nav" class="layui-nav layui-nav-tree" lay-filter="kitNavbar" kit-navbar></ul>
 *     </div>
 * </div>
 *
 * JS:
 * var navbar = layui.navbar;
 * navbar.set({
 *     remote: {
 *         url: ''
 *     },
 *     elem: '#nav'
 * });
 * navbar.render();
 */
layui.define(['layer', 'laytpl', 'element'], function (exports) {
    var $ = layui.jquery,
        layer = layui.layer,
        laytpl = layui.laytpl,
        element = layui.element,
        _modName = 'navbar',
        _doc = $(document);

    var navbar = {
        v: '1.0.0',
        config: {
            data: undefined, //静态数据
            remote: {
                url: undefined, //接口地址
                type: 'GET', //请求方式
                jsonp: false //跨域
            },
            elem: undefined //容器
        },

        set: function (options) {
            var that = this;
            that.config.data = undefined;
            $.extend(true, that.config, options);
            return that;
        },

        /* 是否已设置了elem */
        hasElem: function () {
            var that = this,
                _config = that.config;
            if (_config.elem === undefined && _doc.find('ul[kit-navbar]').length === 0 && $(_config.elem)) {
                layui.hint().error('navbar error: 请配置navbar容器.');
                return false;
            }
            return true;
        },

        /* 获取容器的jq对象 */
        getElem: function () {
            var _config = this.config;
            return (_config.elem !== undefined && $(_config.elem).length > 0) ? $(_config.elem) : _doc.find('ul[kit-navbar]');
        },

        /* 渲染navbar */
        render: function (callback) {
            var that = this,
                _config = that.config, //配置
                _remote = _config.remote, //远程参数配置
                // title:标题, icon:图标, spread:是否展开子菜单, children:子节点
                _tpl = [
                    '{{# layui.each(d,function(index, item){ }}',
                    '{{# if(item.spread){ }}',
                    '<li class="layui-nav-item layui-nav-itemed">',
                    '{{# }else{ }}',
                    '<li class="layui-nav-item">',
                    '{{# } }}',
                    '{{# var hasChildren = item.children!==undefined && item.children.length>0; }}',
                    '{{# if(hasChildren){ }}',
                    '<a href="javascript:;">',
                    '{{# if(item.icon!==undefined){ }}',
                    '{{# if (item.icon.indexOf("fa-") !== -1) { }}',
                    '<i class="fa {{item.icon}}" aria-hidden="true"></i>',
                    '{{# } else { }}',
                    '<i class="layui-icon {{item.icon}}"></i>',
                    '{{# } }}',
                    '{{# } }}',
                    '<span>&nbsp;{{item.title}}</span>',
                    '</a>',
                    '{{# var children = item.children; }}',
                    '<dl class="layui-nav-child">',
                    '{{# layui.each(children,function(childIndex, child){ }}',
                    '<dd>',
                    '<a href="{{child.url}}">',
                    '{{# if(child.icon!==undefined){ }}',
                    '{{# if (child.icon.indexOf("fa-") !== -1) { }}',
                    '<i class="fa {{child.icon}}" aria-hidden="true"></i>',
                    '{{# } else { }}',
                    '<i class="layui-icon {{child.icon}}"></i>',
                    '{{# } }}',
                    '<span>&nbsp;{{child.title}}</span>',
                    '{{# } else { }}',
                    '<span>&nbsp;&nbsp;&nbsp;&nbsp;{{child.title}}</span>',
                    '{{# } }}',
                    '</a>',
                    '</dd>',
                    '{{# }); }}',
                    '</dl>',
                    '{{# }else{ }}',
                    '<a href="{{item.url}}">',
                    '{{# if(item.icon!==undefined){ }}',
                    '{{# if (item.icon.indexOf("fa-") !== -1) { }}',
                    '<i class="fa {{item.icon}}" aria-hidden="true"></i>',
                    '{{# } else { }}',
                    '<i class="layui-icon {{item.icon}}"></i>',
                    '{{# } }}',
                    '<span>&nbsp;{{item.title}}</span>',
                    '{{# } else { }}',
                    '<span>&nbsp;&nbsp;&nbsp;&nbsp;{{item.title}}</span>',
                    '{{# } }}',
                    '</a>',
                    '{{# } }}',
                    '</li>',
                    '{{# }); }}'
                ], //模板
                _data = [];
            var navbarLoadIndex = layer.load(2);
            if (!that.hasElem()) {
                return that;
            }
            var _elem = that.getElem();
            //本地数据优先
            if (_config.data !== undefined && _config.data.length > 0) {
                _data = _config.data;
            } else {
                var dataType = _remote.jsonp ? 'jsonp' : 'json';
                var options = {
                    url: _remote.url,
                    type: _remote.type,
                    error: function (xhr, status, thrown) {
                        layui.hint().error('navbar error: ajax请求出错.' + thrown);
                        navbarLoadIndex && layer.close(navbarLoadIndex);
                    },
                    success: function (res) {
                        _data = res;
                    }
                };
                $.extend(true, options, _remote.jsonp ? {
                    dataType: 'jsonp',
                    jsonp: 'callback', //请求接口的参数名
                    jsonpCallback: 'callback' //要执行的回调函数
                } : {
                    dataType: 'json'
                });
                $.support.cors = true;
                $.ajax(options);
            }
            var tIndex = setInterval(function () {
                if (_data.length > 0) {
                    clearInterval(tIndex);
                    //渲染模板
                    laytpl(_tpl.join('')).render(_data, function (html) {
                        _elem.html(html);

                        //解决站点url刷新后菜单的位置问题 start 2018/12/12 update
                        var arrPath = window.location.pathname.split("/");
                        if (arrPath.length > 1) {
                            var relUrl = '/' + arrPath[1];
                            $(that.config.elem + ' a').each(function () { //遍历
                                var that = this;
                                if ($(that).attr('href') === relUrl) {
                                    $(that).parent().addClass('layui-this');
                                    $(that).parents("li:eq(0)").addClass('layui-nav-itemed');
                                    var nodes = $(that).parents("li:eq(0)").find('a .layui-nav-more');
                                    if (nodes.length > 0) {
                                        nodes.each(function () {
                                            if ($(this).parents("dd:eq(0)").find("[href='" + relUrl + "']").length > 0) {
                                                $(this).parent().parent().addClass('layui-nav-itemed');
                                            }
                                        });
                                    }
                                }
                            });
                        }
                        //解决站点url刷新后菜单的位置问题 end

                        element.init();
                        //关闭等待层
                        navbarLoadIndex && layer.close(navbarLoadIndex);
                    });
                }
            }, 50);
            return that;
        }
    };
    exports(_modName, navbar);
});