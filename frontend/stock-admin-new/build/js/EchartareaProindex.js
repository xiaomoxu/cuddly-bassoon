
function init_echartareapro() {

    if (typeof (echarts) === 'undefined') {
        return;
    }

    var echart_areapro = echarts.init(document.getElementById('echart_areapro'))
    
    var xAxisdatapro=[],seriesdatapro=[]

    $.when(
        $.ajax({
            type: "get",
            async: false,
            url: "http://10.20.118.28:9002/stock-static/province",
            dataType: "json",
            timeout: 1000,
            beforeSend: function (xhr) {
                echart_areapro.showLoading();
            },
            success: function (dataresult) {
                echart_areapro.hideLoading();
                console.log(dataresult.length)
                if (dataresult) {
                    for (var i = 0; i < dataresult.length; i++) {
                        xAxisdatapro.push(dataresult[i].key);
                        seriesdatapro.push(dataresult[i].count);
                    }
                }
                console.log(xAxisdatapro);
            },
            error: function (errorMsg) {
                console.log("zz data get failed");
            }
        })).done(function () {

        echart_areapro.hideLoading();
        console.log("done");
        
        echart_areapro.setOption({
            
                    backgroundColor: '',
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    legend: {
                        data: ['上市公司省级分布图'],
                        textStyle: {
                            color: '#ccc'
                        }
                    },
                    xAxis: {
                        data: xAxisdatapro,
                        axisLine: {
                            lineStyle: {
                                color: '#ccc'
                            }
                        },
                        axisLabel:{
                          interval:0  
                        }
                    },
                    yAxis: {
                        splitLine: {show: false},
                        axisLine: {
                            lineStyle: {
                                color: '#ccc'
                            }
                        }
                    },
                    series: [
                   {
                        name: '上市公司省级分布图',
                        type: 'bar',
                        barWidth: 30,
                        itemStyle: {
                            normal: {
                                label:{
                                    show: "true",
                                    position: 'top',
                                    textStyle: {
                                                color:"#0f0101",
                                                fontStyle:"normal",
                                                fontWeight:"normal",
                                                fontSize:16
                                        }
                                },
                                barBorderRadius: 3,
                                color: new echarts.graphic.LinearGradient(
                                    0, 0, 0, 1,
                                    [
                                        {offset: 0, color: '#394d4e'},
                                        {offset: 1, color: '#43eec6'}
                                    ]
                                )
                            }
                        },
                        data: seriesdatapro
                    },{
                    name:'上市公司省级分布图',
                    type:'line',
                     itemStyle : {
                                normal : {
                                   color:'#ed193a'
                                }
                            },
                    data:seriesdatapro,
                    }]
                       
            
        });        
    }).fail(function () {
        echart_areapro.hideLoading();
        alert("we failed to get the data for this echart");        
        console.log("we failed to get the data for this echart!");　　　　
    });
    

}



$(document).ready(function () {
    init_sidebar();
    init_echartareapro();
});