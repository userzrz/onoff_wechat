$(document).ready(function () {
    $(".div_month").hide();
    $("#span_sum").hide();
    //判断是否有积分
    $(".sup-sup>div:first-child>span").css("display","none");
    var value=$(".div_week>div:first-child>span").text();
    if(value==null||value==''){
        $(".sup-sup>div:first-child>span").css("display","inline-block");
        $(".sup-sup>div:first-child>span").text("您在本周还没有积分噢！返回查看如何获取更多积分");
    }
    $("#week_list").click(function () {
        $("#week_list").css({"background-color": "rgba(255, 255, 255, 0.5)", "color": "black"});
        $("#month_list").css({"background-color": "rgba(000, 000, 000, 0.5)", "color": "#efefef"});
        $(".div_week").show();
        $("#span_weeksum").show();
        $(".div_month").hide();
        $("#span_sum").hide();
        $(".sup-sup>div:first-child>span").css("display","none");
        var value=$(".div_week>div:first-child>span").text();
        if(value==null||value==''){
            $(".sup-sup>div:first-child>span").css("display","inline-block");
            $(".sup-sup>div:first-child>span").text("您在本周还没有积分噢！返回查看如何获取更多积分");
        }
    });

    $("#month_list").click(function () {
        $("#week_list").css({"background-color": "rgba(000, 000, 000, 0.5)", "color": "#efefef"});
        $("#month_list").css({"background-color": "rgba(255, 255, 255, 0.5)", "color": "black"});
        $(".div_month").show();
        $(".div_week").hide();
        $("#span_sum").show();
        $("#span_weeksum").hide();
        $(".sup-sup>div:first-child>span").css("display","none");
        var value=$(".div_month>div:first-child>span").text();
        if(value==null||value==''){
            $(".sup-sup>div:first-child>span").css("display","inline-block");
            $(".sup-sup>div:first-child>span").text("您在本月还没有积分噢！返回查看如何获取更多积分");
        }
    });
});
