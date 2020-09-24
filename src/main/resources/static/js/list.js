$(document).ready(function () {
    //初始化隐藏其他活动周期
    $("#span_issue>span").hide();
    $("#span_issue>span:eq(0)").show();
    //设置第一名有个小皇冠
    var value = $(".div_crown").attr("title");
    if (value == 1) {
        $(".div_crown").parent().css({"padding-top": "1.5rem", "height": "4.3rem"});
    }
    $("[title='me']").css("background-color", "rgba(000, 000, 000, 0.2)");
    //初始化隐藏月榜信息
    $("#sup_sup_2").hide();

    //点击积分周榜
    $("#week_list").click(function () {
        $("#sup_sup_2").hide();
        $("#sup_sup_1").show();
        $("#week_list").css({"background-color": "rgba(255, 255, 255, 0.5)", "color": "black"});
        $("#month_list").css({"background-color": "rgba(000, 000, 000, 0.5)", "color": "#efefef"});
        //初始化隐藏其他活动周期
        $("#span_issue>span").hide();
        $("#span_issue>span:eq(0)").show();
    });
    //点击积分月榜
    $("#month_list").click(function () {
        $("#sup_sup_1").hide();
        $("#sup_sup_2").show();
        $("#week_list").css({"background-color": "rgba(000, 000, 000, 0.5)", "color": "#efefef"});
        $("#month_list").css({"background-color": "rgba(255, 255, 255, 0.5)", "color": "black"});
        //初始化隐藏其他活动周期
        $("#span_issue>span").hide();
        $("#span_issue>span:eq(1)").show();
    });

    //取消下拉列表最后一个li的下边框
    $("#div_2_brother_1 li").last().css("border-bottom", "none");
    $("#div_2_brother_2 li").last().css("border-bottom", "none");

    var but1 = false;
    var but2 = false;
    $("#div_2_brother>div").css({"visibility": "hidden", "display": "none"});
    //点击历史周榜按钮
    $("#div_2_but_1").click(function () {
        $("#div_2_but_2").css({"box-shadow": "3px 2px rgba(000, 000, 000, 0.7)","left":"0","top":"0"});
        $("#div_2_but_1").css({"box-shadow": "none", "position": "relative","left":"3px","top":"2px"});
        if (but1) {
            $("#div_2_brother>div").css({"visibility": "hidden", "display": "none"});
            but1 = false;
            but2 = false;
            $("#div_2_but_1").css({"box-shadow": "3px 2px rgba(000, 000, 000, 0.7)","left":"0","top":"0"});
        } else {
            $("#div_2_brother_2").css({"visibility": "hidden"});
            $("#div_2_brother_1").css({"display": "inline-block", "visibility": "visible"});
            but1 = true;
            but2 = false;
        }
    });
    //点击历史月榜按钮
    $("#div_2_but_2").click(function () {
        $("#div_2_but_1").css({"box-shadow": "3px 2px rgba(000, 000, 000, 0.7)","left":"0","top":"0"});
        $("#div_2_but_2").css({"box-shadow": "none", "position": "relative","left":"3px","top":"2px"});
        if (but2) {
            $("#div_2_brother>div").css({"visibility": "hidden", "display": "none"});
            but2 = false;
            but1 = false;
            $("#div_2_but_2").css({"box-shadow": "3px 2px rgba(000, 000, 000, 0.7)","left":"0","top":"0"});
        } else {
            $("#div_2_brother_1").css({"visibility": "hidden"});
            $("#div_2_brother_2").css({"display": "inline-block", "visibility": "visible"});
            but2 = true;
            but1 = false;
        }
    });
    //提交请求 w 历史周 m 历史月 k本周 h本月
    $(".dropdown-item").click(function () {
        var url;
        var id = $(this).attr("id");
        var type = $(this).attr("value");
        if (type == "week") {
            //回弹按钮
            $("#div_2_but_1").css({"box-shadow": "3px 2px rgba(000, 000, 000, 0.7)","left":"0","top":"0"});
            url = '/past/' + id + "w";
            $.ajax({
                url: url,
                type: 'GET',
                success: function (data) {
                    $("#sup_sup_1").html(data);
                    $("#sup_sup_1").show();
                    $("#sup_sup_2").hide();
                    var value = $(".div_crown").attr("title");
                    if (value == 1) {
                        $(".div_crown").parent().css({"padding-top": "1.5rem", "height": "4.3rem"});
                    }
                    var Cycle = $("#Cycle").text();
                    $("#span_issue>span:eq(0)").text(Cycle);
                    console.log(Cycle);
                    $("#span_issue>span").hide();
                    $("#span_issue>span:eq(0)").show();
                }
            });
            $("#week_list").css({"background-color": "rgba(255, 255, 255, 0.5)", "color": "black"});
            $("#month_list").css({"background-color": "rgba(000, 000, 000, 0.5)", "color": "#efefef"});
        } else if (type == "month") {
            //回弹按钮
            $("#div_2_but_2").css({"box-shadow": "3px 2px rgba(000, 000, 000, 0.7)","left":"0","top":"0"});
            url = '/past/' + id + "m";
            $.ajax({
                url: url,
                type: 'GET',
                success: function (data) {
                    $("#sup_sup_2").html(data);
                    $("#sup_sup_1").hide();
                    $("#sup_sup_2").show();
                    var value = $(".div_crown").attr("title");
                    if (value == 1) {
                        $(".div_crown").parent().css({"padding-top": "1.5rem", "height": "4.3rem"});
                    }
                    var Cycle = $("#Cycle2").text();
                    $("#span_issue>span:eq(1)").text(Cycle);
                    console.log(Cycle);
                    $("#span_issue>span").hide();
                    $("#span_issue>span:eq(1)").show();
                }
            });
            $("#week_list").css({"background-color": "rgba(000, 000, 000, 0.5)", "color": "#efefef"});
            $("#month_list").css({"background-color": "rgba(255, 255, 255, 0.5)", "color": "black"});
        } else if (type == "thisWeek") {
            //回弹按钮
            $("#div_2_but_1").css({"box-shadow": "3px 2px rgba(000, 000, 000, 0.7)","left":"0","top":"0"});
            url = '/past/' + id + "k";
            $.ajax({
                url: url,
                type: 'GET',
                success: function (data) {
                    $("#sup_sup_1").html(data);
                    $("#sup_sup_1").show();
                    $("#sup_sup_2").hide();
                    var value = $(".div_crown").attr("title");
                    if (value == 1) {
                        $(".div_crown").parent().css({"padding-top": "1.5rem", "height": "4.3rem"});
                    }
                    $("#sup_sup_1 [title='me']").css("background-color", "rgba(000, 000, 000, 0.2)");
                    var Cycle = $("#Cycle").text();
                    $("#span_issue>span:eq(0)").text(Cycle);
                    console.log(Cycle);
                    $("#span_issue>span").hide();
                    $("#span_issue>span:eq(0)").show();
                }
            });
            $("#week_list").css({"background-color": "rgba(255, 255, 255, 0.5)", "color": "black"});
            $("#month_list").css({"background-color": "rgba(000, 000, 000, 0.5)", "color": "#efefef"});
        } else if (type == "thisMonth") {
            //回弹按钮
            $("#div_2_but_2").css({"box-shadow": "3px 2px rgba(000, 000, 000, 0.7)","left":"0","top":"0"});
            url = '/past/' + id + "h";
            $.ajax({
                url: url,
                type: 'GET',
                success: function (data) {
                    $("#sup_sup_2").html(data);
                    $("#sup_sup_1").hide();
                    $("#sup_sup_2").show();
                    var value = $(".div_crown").attr("title");
                    if (value == 1) {
                        $(".div_crown").parent().css({"padding-top": "1.5rem", "height": "4.3rem"});
                    }
                    $("#sup_sup_2 [title='me']").css("background-color", "rgba(000, 000, 000, 0.2)");
                    var Cycle = $("#Cycle2").text();
                    console.log(Cycle);
                    $("#span_issue>span:eq(1)").text(Cycle);
                    $("#span_issue>span").hide();
                    $("#span_issue>span:eq(1)").show();
                }
            });
            $("#week_list").css({"background-color": "rgba(000, 000, 000, 0.5)", "color": "#efefef"});
            $("#month_list").css({"background-color": "rgba(255, 255, 255, 0.5)", "color": "black"});
        }
        $("#div_2_brother>div").css({"visibility": "hidden"});
        but1 = false;
        but2 = false;
    });

    //点击按钮按压及回弹 but_ex but_ru but_re but_co
    $("#but_ex").click(function () {
        $("#but_ex").css({"box-shadow": "none", "position": "relative","left":"3px","top":"2px"});
        setTimeout(function () {
            $("#but_ex").css({"box-shadow": "3px 2px rgba(000, 000, 000, 0.7)","left":"0","top":"0"});
        },500);
    });
    $("#but_ru").click(function () {
        $("#but_ru").css({"box-shadow": "none", "position": "relative","left":"3px","top":"2px"});
        setTimeout(function () {
            $("#but_ru").css({"box-shadow": "3px 2px rgba(000, 000, 000, 0.7)","left":"0","top":"0"});
        },500);
    });
    $("#but_re").click(function () {
        $("#but_re").css({"box-shadow": "none", "position": "relative","left":"3px","top":"2px"});
        setTimeout(function () {
            $("#but_re").css({"box-shadow": "3px 2px rgba(000, 000, 000, 0.7)","left":"0","top":"0"});
        },500);
    });
    $("#but_co").click(function () {
        $("#but_co").css({"box-shadow": "none", "position": "relative","left":"3px","top":"2px"});
        setTimeout(function () {
            $("#but_co").css({"box-shadow": "3px 2px rgba(000, 000, 000, 0.7)","left":"0","top":"0"});
        },500);
    });
});


