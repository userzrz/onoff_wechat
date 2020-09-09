$(document).ready(function () {
    $(".div_month").hide();
    $("#span_sum").hide();
    $("#week_list").click(function () {
        $("#week_list").css({"background-color": "rgba(255, 255, 255, 0.5)", "color": "black"});
        $("#month_list").css({"background-color": "rgba(000, 000, 000, 0.5)", "color": "#efefef"});
        $(".div_week").show();
        $(".div_month").hide();
        $("#span_sum").hide();
        $("#span_weeksum").show();
        if(time2==null||time2==''){
            $("#h3_1").html("您在本周还没有积分哦！快去邀请好友获取积分吧");
        }
    });

    $("#month_list").click(function () {
        $("#week_list").css({"background-color": "rgba(000, 000, 000, 0.5)", "color": "#efefef"});
        $("#month_list").css({"background-color": "rgba(255, 255, 255, 0.5)", "color": "black"});
        $(".div_month").show();
        $(".div_week").hide();
        $("#span_sum").show();
        $("#span_weeksum").hide();
        if(time1==null||time1==''){
            $("#h3_1").html("您还没有积分 快去邀请好友获取积分吧");
        }
    });
});
