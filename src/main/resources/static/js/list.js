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
    //点击历史周榜按钮
    $("#div_2_but_1").click(function () {
        if (but1) {
            $("#div_2_brother_1").css({"display": "none"});
            $("#div_2_brother_2").css({"display": "none"});
            but1 = false;
            but2 = false;
        } else {
            $("#div_2_brother_1").css({"display": "inline-block", "visibility": "visible"});
            $("#div_2_brother_2").css({"display": "inline-block", "visibility": "hidden"});
            but1 = true;
            but2 = false;
        }
    });
    //点击历史月榜按钮
    $("#div_2_but_2").click(function () {
        if (but2) {
            $("#div_2_brother_1").css({"display": "none"});
            $("#div_2_brother_2").css({"display": "none"});
            but2 = false;
            but1 = false;
        } else {
            $("#div_2_brother_2").css({"display": "inline-block", "visibility": "visible"});
            $("#div_2_brother_1").css({"display": "inline-block", "visibility": "hidden"});
            but2 = true;
            but1 = false;
        }
    });
    //提交请求
    $(".dropdown-item").click(function () {
        var url;
        var id = $(this).attr("id");
        var type = $(this).attr("value");
        if (type == "week") {
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
                    var Cycle=$("#Cycle").text();
                    $("#span_issue>span:eq(0)").text(Cycle);
                }
            });
            $("#week_list").css({"background-color": "rgba(255, 255, 255, 0.5)", "color": "black"});
            $("#month_list").css({"background-color": "rgba(000, 000, 000, 0.5)", "color": "#efefef"});
        }else if (type == "month") {
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
                    var Cycle=$("#Cycle").text();
                    $("#span_issue>span:eq(1)").text(Cycle);
                }
            });
            $("#week_list").css({"background-color": "rgba(000, 000, 000, 0.5)", "color": "#efefef"});
            $("#month_list").css({"background-color": "rgba(255, 255, 255, 0.5)", "color": "black"});
        }else if (type=="thisWeek") {
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
                    var Cycle=$("#Cycle").text();
                    $("#span_issue>span:eq(0)").text(Cycle);
                }
            });
            $("#week_list").css({"background-color": "rgba(255, 255, 255, 0.5)", "color": "black"});
            $("#month_list").css({"background-color": "rgba(000, 000, 000, 0.5)", "color": "#efefef"});
        }else if (type=="thisMonth") {
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
                    var Cycle=$("#Cycle").text();
                    $("#span_issue>span:eq(1)").text(Cycle);
                }
            });
            $("#week_list").css({"background-color": "rgba(000, 000, 000, 0.5)", "color": "#efefef"});
            $("#month_list").css({"background-color": "rgba(255, 255, 255, 0.5)", "color": "black"});
        }
        $("#div_2_brother_1").css({"display": "none"});
        $("#div_2_brother_2").css({"display": "none"});
        but1 = false;
        but2 = false;
    });
});


