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

function init_echart() {

    if (typeof (echarts) === 'undefined') {
        return;
    }

    var echartLine = echarts.init(document.getElementById('echart_line'))
    var xAxisdata = [],seriesdata_hs = [],seriesdata_zz = [],seriesdata_rotation=[],seriesdata_rotation2=[]

    $.when(
        $.ajax({
            type: "get",
            async: false,
            url: "http://10.20.118.28:9002/tow-eight-rotation",
            dataType: "json",
            timeout: 1000,
            beforeSend: function (xhr) {
                echartLine.showLoading();
            },
            success: function (dataresult) {
                echartLine.hideLoading();
                console.log(dataresult)
                if (dataresult) {
                    for (var i = 0; i < dataresult.length; i++) {
                        xAxisdata.push(dataresult[i].date);
                        seriesdata_hs.push(dataresult[i].hsMoney);
                        seriesdata_zz.push(dataresult[i].zzMoney);
                        seriesdata_rotation.push(dataresult[i].rotationMoney);
                        seriesdata_rotation2.push(dataresult[i].rotationMoeny2);
                    }
                }
            },
            error: function (errorMsg) {
                console.log("zz data get failed");
            }
        })).done(function () {

        echartLine.hideLoading();
        console.log("done");
        
        echartLine.setOption({
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['沪深300','中证500','R1','R2']
            },
            grid: {
                left: '3%',
                right: '3%',
                bottom: '10%',
                containLabel: true
            },
            toolbox: {
                show: true,
                feature: {
                    mark: {
                        show: true
                    },
                    magicType: {
                        show: true,
                        type: ['line', 'bar', 'stack', 'tiled']
                    },
                    restore: {
                        show: true
                    }
                }
            },
            calculable: true,
            dataZoom: {
                show: true,
                realtime: true,
                start: 0,
                end: 100
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: xAxisdata
            },
            yAxis:
                {
                    type: 'value'
                    },
            series: [
                {
                    name: '沪深300',
                    type: 'line',
                    data: seriesdata_hs
                        },
                {
                    name: '中证500',
                    type: 'line',
                    data: seriesdata_zz
                        },
                {
                    name: 'R1',
                    type: 'line',
                    data: seriesdata_rotation
                        },
                {
                    name: 'R2',
                    type: 'line',
                    data: seriesdata_rotation2
                        }
                
               ]
        });        
        /* 
        //debug info
        var tmp11 = echartLine.getOption();
        var dz = tmp11.dataZoom[0];
        console.log(tmp11);
        console.log(dz);
        //debug info
        */
    }).fail(function () {
        echartLine.hideLoading();
        alert("we failed to get the data for this echart");        
        console.log("we failed to get the data for this echart!");　　　　
    });

}



$(document).ready(function () {
    console.log("EchartLocal.js")
    init_sidebar();
    init_echart();
});