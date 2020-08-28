$(document).ready(function () {
    var time1 = $("#span_time1").text();
    var time2 = $("#span_time2").text();
    $("#h3_1").html("");
    $(".div_weekRecord").hide();
    if(time1==null||time1==''){
        $("#h3_1").html("您还没有积分 快去邀请好友获取积分吧");
    }
    $("#but_head").css("background-color","white");
    $("#but_head").click(function () {
        $("#h3_1").html("");
        $(".div_Record").show();
        $(".div_weekRecord").hide();
        if(time1==null||time1==''){
            $("#h3_1").html("您还没有积分 快去邀请好友获取积分吧");
        }
        $("#but_head").css("background-color","white");
        $("#but_head2").css("background-color","#efefef");
    });
    $("#but_head2").click(function () {
        $(".div_weekRecord").show();
        $(".div_Record").hide();
        if(time2==null||time2==''){
            $("#h3_1").html("您在本周还没有积分哦！快去邀请好友获取积分吧");
        }
        $("#but_head2").css("background-color","white");
        $("#but_head").css("background-color","#efefef");
    });
});
