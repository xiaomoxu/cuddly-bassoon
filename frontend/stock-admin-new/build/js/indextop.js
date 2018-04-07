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

$(document).ready(function () {
    console.log("load dada for index top")
    init_indextop();    
});