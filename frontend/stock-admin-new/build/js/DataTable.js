(function ($, sr) {
    // debouncing function from John Hann
    // http://unscriptable.com/index.php/2009/03/20/debouncing-javascript-methods/
    var debounce = function (func, threshold, execAsap) {
        var timeout;

        return function debounced() {
            var obj = this,
                args = arguments;

            function delayed() {
                if (!execAsap)
                    func.apply(obj, args);
                timeout = null;
            }

            if (timeout)
                clearTimeout(timeout);
            else if (execAsap)
                func.apply(obj, args);

            timeout = setTimeout(delayed, threshold || 100);
        };
    };

    // smartresize 
    jQuery.fn[sr] = function (fn) {
        return fn ? this.bind('resize', debounce(fn)) : this.trigger(sr);
    };

})(jQuery, 'smartresize');




// Sidebar
function init_sidebar() {

    // TODO: This is some kind of easy fix, maybe we can improve this
    $("#sidebar-menu").load("menu.html", function () {

        var CURRENT_URL = window.location.href.split('#')[0].split('?')[0],
            $BODY = $('body'),
            $MENU_TOGGLE = $('#menu_toggle'),
            $SIDEBAR_MENU = $('#sidebar-menu'),
            $SIDEBAR_FOOTER = $('.sidebar-footer'),
            $LEFT_COL = $('.left_col'),
            $RIGHT_COL = $('.right_col'),
            $NAV_MENU = $('.nav_menu'),
            $FOOTER = $('footer');


        var setContentHeight = function () {
            // reset height
            $RIGHT_COL.css('min-height', $(window).height());

            var bodyHeight = $BODY.outerHeight(),
                footerHeight = $BODY.hasClass('footer_fixed') ? -10 : $FOOTER.height(),
                leftColHeight = $LEFT_COL.eq(1).height() + $SIDEBAR_FOOTER.height(),
                contentHeight = bodyHeight < leftColHeight ? leftColHeight : bodyHeight;

            // normalize content
            contentHeight -= $NAV_MENU.height() + footerHeight;

            $RIGHT_COL.css('min-height', contentHeight);
        };

        $SIDEBAR_MENU.find('a').on('click', function (ev) {
            console.log('clicked - sidebar_menu');
            var $li = $(this).parent();

            if ($li.is('.active')) {
                $li.removeClass('active active-sm');
                $('ul:first', $li).slideUp(function () {
                    setContentHeight();
                });
            } else {
                // prevent closing menu if we are on child menu
                if (!$li.parent().is('.child_menu')) {
                    $SIDEBAR_MENU.find('li').removeClass('active active-sm');
                    $SIDEBAR_MENU.find('li ul').slideUp();
                } else {
                    if ($BODY.is(".nav-sm")) {
                        $SIDEBAR_MENU.find("li").removeClass("active active-sm");
                        $SIDEBAR_MENU.find("li ul").slideUp();
                    }
                }
                $li.addClass('active');

                $('ul:first', $li).slideDown(function () {
                    setContentHeight();
                });
            }
        });

        // toggle small or large menu 
        $MENU_TOGGLE.on('click', function () {
            console.log('clicked - menu toggle');

            if ($BODY.hasClass('nav-md')) {
                $SIDEBAR_MENU.find('li.active ul').hide();
                $SIDEBAR_MENU.find('li.active').addClass('active-sm').removeClass('active');
            } else {
                $SIDEBAR_MENU.find('li.active-sm ul').show();
                $SIDEBAR_MENU.find('li.active-sm').addClass('active').removeClass('active-sm');
            }

            $BODY.toggleClass('nav-md nav-sm');

            setContentHeight();

            $('.dataTable').each(function () {
                $(this).dataTable().fnDraw();
            });
        });

        // check active menu
        $SIDEBAR_MENU.find('a[href="' + CURRENT_URL + '"]').parent('li').addClass('current-page');

        $SIDEBAR_MENU.find('a').filter(function () {
            return this.href == CURRENT_URL;
        }).parent('li').addClass('current-page').parents('ul').slideDown(function () {
            setContentHeight();
        }).parent().addClass('active');

        // recompute content when resizing
        $(window).smartresize(function () {
            setContentHeight();
        });

        setContentHeight();

        // fixed sidebar
        if ($.fn.mCustomScrollbar) {
            $('.menu_fixed').mCustomScrollbar({
                autoHideScrollbar: true,
                theme: 'minimal',
                mouseWheel: {
                    preventDefault: true
                }
            });
        }
    })
};
// /Sidebar

function init_DataTables() {

    if (typeof ($.fn.DataTable) === 'undefined') {
        return;
    }
    console.log('init_DataTables');


    var handleDataTableButtons = function () {
        if ($("#datatable-buttons").length) {
            $("#datatable-buttons").DataTable({
                dom: "Blfrtip",
                buttons: [
                    {
                        extend: "copy",
                        className: "btn-sm"
						},
                    {
                        extend: "csv",
                        className: "btn-sm"
						},
                    {
                        extend: "excel",
                        className: "btn-sm"
						},
                    {
                        extend: "pdfHtml5",
                        className: "btn-sm"
						},
                    {
                        extend: "print",
                        className: "btn-sm"
						},
					  ],
                responsive: true
            });
        }
    };

    TableManageButtons = function () {
        "use strict";
        return {
            init: function () {
                handleDataTableButtons();
            }
        };
    }();


    //$('#datatable').dataTable();
    //add by wp
    $('#datatable').dataTable({
        "ajax": {
            "type": "GET",
            "url": "http://10.20.118.28:9002/stocklist-spark",
            //"url": "table.json",
            "dataType": "json",
            "contentType": "application/x-www-form-urlencoded; charset=UTF-8"
        },
        "columns": [

            {
                "title": "Code",
                "data": "code"
            },
            {
                "title": "Name",
                "data": "name"
            },
            {
                "title": "Market",
                "data": "market"
            },
            {
                "title": "Region",
                "data": "region"
            },
            {
                "title": "Block",
                "data": "belongTo"
            },
            {
                "title": "Weight",
                "data": "weight"
            },
            {
                "title": "latest price",
                "data": "currentPrice"
            },
            {
                "title": "EPS",
                "data": "eps"
            },
            {
                "title": "BVPS",
                "data": "bvps"
            },
            {
                "title": "ROE",
                "data": "roe"
            },
            {
                "title": "TotalStock",
                "data": "totalStock"
            },
            {
                "title": "LIQUI",
                "data": "liqui"
            },
            {
                "title": "LTSZ",
                "data": "ltsz"
            },
            //{"title":"详细地址", "data": "ltsz"}
            ],
/*
        "columnDefs": [
            {
                "render": function (data, type, row) {
                    //data为当前列的数据
                    //type为当前列数据类型
                    //row为当前行数据
                    var rowData = JSON.stringify(row);
                    var str = "<a class='herf='javascript:void(0)' onclick='changeModal(" + rowData + ")'>联系人</a>";
                    return str;
                    //此处return可自己定义，博主此处举例为超链接，传参须注意，若传字符串需加上转义字符，否则会报错ReferenceError: XXX is not defined at HTMLAnchorElement.onclick
                },
                //此处target负数表示从右向左的顺序
                //-1为右侧第一列 
                "targets": -1
                   }
                  ],
        "createdRow": function (row, data, index) {
            ////创建行之后的操作
        },
        */
    });
    //add by wp 


    $('#datatable-keytable').DataTable({
        keys: true
    });

    $('#datatable-responsive').DataTable();


    $('#datatable-scroller').DataTable({
        ajax: "js/datatables/json/scroller-demo.json",
        deferRender: true,
        scrollY: 380,
        scrollCollapse: true,
        scroller: true
    });

    $('#datatable-fixed-header').DataTable({
        fixedHeader: true
    });

    var $datatable = $('#datatable-checkbox');

    $datatable.dataTable({
        'order': [[1, 'asc']],
        'columnDefs': [
            {
                orderable: false,
                targets: [0]
            }
				  ]
    });

    $datatable.on('draw.dt', function () {
        $('checkbox input').iCheck({
            checkboxClass: 'icheckbox_flat-green'
        });
    });

    TableManageButtons.init();




};

$(document).ready(function () {

    console.log("DataTable.js")
    init_sidebar();
    init_DataTables();
});