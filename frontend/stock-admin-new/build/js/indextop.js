function init_indextop() {
    $.when(
        $.ajax({
            type: "get",
            async: false,
            url: "http://10.20.118.28:9002/stock-index/000001,000300,000905,399006,000016,399001",
            dataType: "json",
            timeout: 1000,
            beforeSend: function (xhr) {                
                
            },
            success: function (dataresult) {                
                console.log(dataresult.length);
                //var sty1="<i class='red'><i class='fa fa-sort-asc'></i>--</i> Than Yesterday’s Close</span>"; 
                //var sty2="<i class='green'><i class='fa fa-sort-desc'></i>12% </i> Than Yesterday’s Close</span>"
                
                for(var i=0, ien=dataresult.length; i<ien ; i++ ){
                    $(".dlt"+i+"").text(toDecimal(dataresult[i].close));
                    if (dataresult[i].change >0){
                    $(".dld"+i+"").append("<i class='red'><i class='fa fa-sort-asc'></i>"+toPercent(dataresult[i].change)+"</i> Than Yesterday’s</span>")                        
                    }else{
                     $(".dld"+i+"").append("<i class='green'><i class='fa fa-sort-desc'></i>"+toPercent(dataresult[i].change)+"</i> Than Yesterday’s</span>")
                    }
                }
            },
            error: function (errorMsg) {                
                console.log("top data get failed");
            }
        })).done(function () {
            console.log("done"); 
        }).fail(function () {
            echartLine.hideLoading();
            alert("we failed to get the data for index top");        
            console.log("we failed to get the data for this echart!");　　　　
        });
}


 function toDecimal(x) { 
  var f = parseFloat(x); 
      if (isNaN(f)) { 
        return; 
      } 
  f = Math.round(x*100)/100; 
  return f; 
 } 

function toPercent(num){
    var str=Number(num*100).toFixed(1);
    str+="%";
    return str;
}

function init_pie1() {

    if (typeof (echarts) === 'undefined') {
        return;
    }

    var echart_indus = echarts.init(document.getElementById('index_indus'))
    
    var seriesDatapie1=[],hiddenValue=[],legendDatapie1=[],tempobj={}
    
    $.when(
        $.ajax({
            type: "get",
            async: false,
            url: "http://10.20.118.28:9002/stock-static/industry",
            dataType: "json",
            timeout: 1000,
            beforeSend: function (xhr) {                
                echart_indus.showLoading();
            },
            success: function (dataresult) {
                echart_indus.hideLoading();
                console.log(dataresult.length)                
                
                if (dataresult) {                    
                    for (var i = 0; i < dataresult.length; i++) {                        
                        seriesDatapie1.push({
                                name: dataresult[i].key,
                                value: dataresult[i].count
                            })
                    }
                    function compare(property){
                        return function(a,b){
                            var value1 = a[property];
                            var value2 = b[property];
                            return value1 - value2;
                        }
                    }
                seriesDatapie1 = seriesDatapie1.sort(compare('value')).reverse() //ordered
                for (var k = 0; k < 5; k++) {//patches---->
                     hiddenValue.push({
                         name: seriesDatapie1[k].name,
                         value:seriesDatapie1[k].value
                     })
                     legendDatapie1.push(seriesDatapie1[k].name)
                     k<= 5 ? tempobj[seriesDatapie1[k].name]=true:tempobj[seriesDatapie1[k].name]=false
                    }
                }
                console.log("pie1")
                console.log(tempobj);
            },
            error: function (errorMsg) {
                console.log("data get failed");
            }
        })).done(function () {

        echart_indus.hideLoading();
        console.log("done");
            
        echart_indus.setOption({
            
            title : {
                    text: '',
                    subtext: '',
                    x:'center'
                },
                tooltip : {
                    show: 'false',
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    type: 'scroll',
                    orient: 'vertical',
                    right: 50,
                    top: 2,
                    bottom: 10,
                    data: legendDatapie1,
                    selected: tempobj
                },
                series : [
                    {
                        name: '行业',
                        type: 'pie',
                        radius : '60%',
                        center: ['50%', '50%'],
                        data: hiddenValue,
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]                     
            
        });        
    }).fail(function () {
        echart_indus.hideLoading();
        alert("we failed to get the data for this echart");        
        console.log("we failed to get the data for this echart!");　　　　
    });
    

}

function init_pie2() {

    if (typeof (echarts) === 'undefined') {
        return;
    }

    var echart_indus = echarts.init(document.getElementById('index_concept'))
    
    var seriesDatapie2=[],hiddenValue2=[],legendDatapie2=[],tempobj2={}
    
    $.when(
        $.ajax({
            type: "get",
            async: false,
            url: "http://10.20.118.28:9002/stock-static/concept",
            dataType: "json",
            timeout: 1000,
            beforeSend: function (xhr) {                
                echart_indus.showLoading();
            },
            success: function (dataresult) {
                echart_indus.hideLoading();
                console.log(dataresult.length)                
                
                if (dataresult) {                    
                    for (var i = 0; i < dataresult.length; i++) {                        
                        seriesDatapie2.push({
                                name: dataresult[i].key,
                                value: dataresult[i].count
                            })
                    }
                    function compare(property){
                        return function(a,b){
                            var value1 = a[property];
                            var value2 = b[property];
                            return value1 - value2;
                        }
                    }
                seriesDatapie2 = seriesDatapie2.sort(compare('value')).reverse() //ordered                   
                for (var k = 0; k < 5; k++) { //patches---->
                     hiddenValue2.push({
                         name: seriesDatapie2[k].name,
                         value:seriesDatapie2[k].value
                     })
                     legendDatapie2.push(seriesDatapie2[k].name)
                     k<= 5 ? tempobj2[seriesDatapie2[k].name]=true:tempobj2[seriesDatapie2[k].name]=false
                    }
                }
                console.log("pie2")
                console.log(tempobj2);
            },
            error: function (errorMsg) {
                console.log("data get failed");
            }
        })).done(function () {

        echart_indus.hideLoading();
        console.log("done");
            
        echart_indus.setOption({
            
            title : {
                    text: '',
                    subtext: '',
                    x:'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    type: 'scroll',
                    orient: 'vertical',
                    right: 50,
                    top: 2,
                    bottom: 10,
                    data: legendDatapie2,
                    selected: tempobj2
                },
                series : [
                    {
                        name: '行业',
                        type: 'pie',
                        radius : '60%',
                        center: ['50%', '50%'],
                        data: hiddenValue2,
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
                    
            
                       
            
        });        
    }).fail(function () {
        echart_indus.hideLoading();
        alert("we failed to get the data for this echart");        
        console.log("we failed to get the data for this echart!");　　　　
    });
    

}

$(document).ready(function () {
    console.log("load dada for index top")
    init_indextop();
    init_pie1();
    init_pie2();
});