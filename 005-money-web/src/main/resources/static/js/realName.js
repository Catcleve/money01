//同意实名认证协议
$(function () {
  $("#agree").click(function () {
    var ischeck = document.getElementById("agree").checked;
    if (ischeck) {
      $("#btnRegist").attr("disabled", false);
      $("#btnRegist").removeClass("fail");
    } else {
      $("#btnRegist").attr("disabled", "disabled");
      $("#btnRegist").addClass("fail");
    }
  });
});

//打开注册协议弹层
function alertBox(maskid, bosid) {
  $("#" + maskid).show();
  $("#" + bosid).show();
}

//关闭注册协议弹层
function closeBox(maskid, bosid) {
  $("#" + maskid).hide();
  $("#" + bosid).hide();
}

//错误提示
function showError(id, msg) {
  $("#" + id + "Ok").hide();
  $("#" + id + "Err").html("<i></i><p>" + msg + "</p>");
  $("#" + id + "Err").show();
  $("#" + id).addClass("input-red");
}

//错误隐藏
function hideError(id) {
  $("#" + id + "Err").hide();
  $("#" + id + "Err").html("");
  $("#" + id).removeClass("input-red");
}

//显示成功
function showSuccess(id) {
  $("#" + id + "Err").hide();
  $("#" + id + "Err").html("");
  $("#" + id + "Ok").show();
  $("#" + id).removeClass("input-red");
}

$(function () {


  //定义标记，获取要使用的元素
  let phoneFlag = false;
  let realNameFlag = false;
  let messageFlag = false;
  let idCardFlag = false;
  let $phone = $("#phone");
  let $realName = $("#realName");
  let $messageCode = $("#messageCode")
  let $idCard = $("#idCard")


  //验证身份证号方法
  function idCardCheck() {
    return function () {
      idCardFlag = false;
      //获取用户输入的身份证号，去除头尾空格
      let idCard = $(this).val().trim();
      console.log("realNameFlag=" + realNameFlag)

      //验证身份证号格式
      if (!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(idCard)) {
        showError("idCard", "请输入正确的身份证号");
        return;
      }
      if (!realNameFlag) {
        showError("idCard", "请输入姓名");
        return;
      }
      idCardFlag = true;
      showSuccess("idCard");
    };
  }

  //验证手机号方法
  function phoneCheck() {
    return function () {
      phoneFlag = false;
      //获取用户输入的手机号，去除头尾空格
      let phone = $(this).val().trim();

      //验证手机号格式
      if (!/^1[1-9]\d{9}$/.test(phone)) {
        showError("phone", "请输入正确的手机号");
        return;
      }
      //长度必须是11位
      if (phone.length !== 11) {
        showError("phone", "请输入正确的手机号");
        return;
      }
      showSuccess("phone");
      phoneFlag = true;
    };
  }

  //验证姓名方法
  function realNameCheck() {
    return function () {
      realNameFlag = false;
      //获取用户输入的真实姓名，去除头尾空格
      let realName = $(this).val().trim();

      // 姓名只能使用中文
      if (!/^[\u4e00-\u9fa5]{0,100}$/.test(realName)) {
        showError("realName", "姓名错误");
        return;
      }

      //姓名长度限定
      if (realName.length < 2 || realName.length > 5) {
        showError("realName", "长度错误");
        return;
      }

      showSuccess("realName");
      realNameFlag = true
      $idCard.blur()
    };
  }

  //验证短信验证码方法
  function messageCodeCheck() {
    return function () {
      messageFlag = false
      let messageCode = $messageCode.val();
      let phone = $phone.val();
      if (messageCode.length !== 6) {

        showError("messageCode", "验证码错误")
        return
      }
      if (/^[0-9]$/.test(messageCode)) {
        showError("messageCode", "验证码错误")
        return;
      }

      //后台验证是否重复
      $.get("/web/loan/page/checkMessage",
          {
            messageCode: messageCode,
            phone: phone
          },
          function (data) {
            if (data.code === 0) {
              showError("messageCode", data.message);
            } else {
              showSuccess("messageCode");
              messageFlag = true;
            }
          },
          'json');

    };
  }

  //发送验证码
  $("#messageCodeBtn").click(function () {

    let _this = $(this);
    console.log(phoneFlag, idCardFlag, realNameFlag)
    //手机号和密码都可用
    if (phoneFlag === true && idCardFlag === true && realNameFlag === true) {
      $.get("/web/loan/page/messageCode",
          {phone: $phone.val()},
          function (data) {
            if (data.code === 1) {
              if (!$(this).hasClass("on")) {
                $.leftTime(60, function (d) {
                  if (d.status) {
                    _this.addClass("on");
                    _this.html((d.s === "00" ? "60" : d.s) + "秒后重新获取");
                    _this.attr("disabled", true)
                  } else {
                    _this.removeClass("on");
                    _this.html("获取验证码");
                    _this.attr("disabled", false)
                  }
                }, true);
              }
              alert(data.message);
            } else {

            }
          },
          'json');
    } else {
      if ($phone.val() == null || $phone.val() === '') {
        showError("phone", "请输入手机号")
      } else if ($realName.val() == null || $realName.val() === '') {
        showError("realName", "请输入姓名")
      } else if ($idCard.val() == null || $idCard.val() === '') {
        showError("idCard", "请输入身份证号")
      }
    }

  });


  //手机号验证，这里采用双重事件，防止有人输入正确之后再次修改数据
  $phone.blur(phoneCheck())
  $phone.keyup(phoneCheck())

  //姓名验证
  $realName.blur(realNameCheck())
  $realName.keyup(realNameCheck())

  //身份证验证
  $idCard.blur(idCardCheck())
  $idCard.keyup(idCardCheck())

  //短信验证码
  $messageCode.blur(messageCodeCheck())
  $messageCode.keyup(messageCodeCheck())

  //点击认证按钮
  $("#btnRegist").click(function () {

    let phone = $phone.val();
    let idCard = $idCard.val();
    let realName = $realName.val();
    // console.log("phoneFlag=" + phoneFlag + "passwordFlag=" + passwordFlag + "messageFlag=" + messageFlag)
    //需要验证手机号码和密码和验证码
    if (phoneFlag === true && idCardFlag === true && messageFlag === true && realNameFlag === true) {

      $.post("/web/loan/page/idCardCheck",
          {
            idCard: idCard,
            realName: realName,
            phone: phone
          },
          function (data) {
            if (data.code === 0) {
              showError("idCard", data.message);
            } else {
              window.location.href = data.message;
            }
          },
          'json');
    } else {
      if (phone == null || phone === '') {
        showError("phone", "请输入手机号")
      } else if (realName == null || realName === '') {
        showError("realName", "请输入姓名")
      } else if (idCard == null || idCard === '') {
        showError("idCard", "请输入身份证号")
      }
    }
  })
})