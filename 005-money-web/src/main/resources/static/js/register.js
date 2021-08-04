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


//打开注册协议弹层
/*function alertBox(maskid, bosid) {
  $("#" + maskid).show();
  $("#" + bosid).show();
}*/

function alertBox(maskid, bosid) {
  alert("同意就行")
}

//关闭注册协议弹层
function closeBox(maskid, bosid) {
  $("#" + maskid).hide();
  $("#" + bosid).hide();
}


//注册协议确认
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


  //定义标记，获取要使用的元素
  let phoneFlag = false;
  let passwordFlag = false;
  let messageFlag = false;
  let $phone = $("#phone");
  let $loginPassword = $("#loginPassword");
  let $messageCode = $("#messageCode")

  //验证密码方法
  function passWordCheck() {
    return function () {
      passwordFlag = false;
      //获取用户输入的手机号，去除头尾空格
      let loginPassword = $(this).val().trim();

      // 密码字符只可使用数字和大小写英文字母
      if (!/^[0-9a-zA-Z]+$/.test(loginPassword)) {
        showError("loginPassword", "密码字符只可使用数字和大小写英文字母");
        return;
      }
      //密码应同时包含英文和数字
      if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)) {
        showError("loginPassword", "密码应同时包含英文和数字");
        return;
      }

      //密码长度必须大于6位
      if (loginPassword.length < 6 || loginPassword.length > 20) {
        showError("loginPassword", "密码长度必须大于6位");
        return;
      }

      showSuccess("loginPassword");
      passwordFlag = true
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

      //后台验证是否重复
      $.get("/web/loan/page/checkRegisterPhone",
          {phone: phone},
          function (data) {
            if (data.code !== 1) {
              phoneFlag = false;
              // console.log("ajax===="+phoneFlag)
              showError("phone", data.message);
            } else {
              //  可以在这里写注册，能够解决再次修改手机号码的问题
              showSuccess("phone");
              phoneFlag = true;
              // console.log("ajax==="+ phoneFlag)
            }
          },
          'json');
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


  //验证码输入验证

  //手机号验证，这里采用双重事件，防止有人输入正确之后再次修改数据
  $phone.blur(phoneCheck())
  $phone.keyup(phoneCheck())

  //密码验证
  $loginPassword.blur(passWordCheck())
  $loginPassword.keyup(passWordCheck())

  //短信验证码
  $messageCode.blur(messageCodeCheck())
  $messageCode.keyup(messageCodeCheck())

  //发送验证码
  $("#messageCodeBtn").click(function () {

    let _this=$(this);
    console.log("phoneFlag ="+ phoneFlag +"---passwordFlag=" + passwordFlag)
    //手机号和密码都可用
    if (phoneFlag === true && passwordFlag === true) {
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
        showError("phone","请输入手机号")
      }else if ($loginPassword.val() == null || $loginPassword.val() === '') {
        showError("loginPassword","请输入密码")
      }
    }

  });

  //点击注册按钮
  $("#btnRegist").click(function () {

    console.log("phoneFlag=" + phoneFlag + "passwordFlag=" + passwordFlag + "messageFlag=" + messageFlag)
    //需要验证手机号码和密码和验证码
    if (phoneFlag === true && passwordFlag === true && messageFlag === true) {

      $.post("/web/loan/page/register",
          {
            phone: $phone.val(),
            loginPassword: $.md5($loginPassword.val())
          },

          function (data) {
            if (data.code === 1) {
              window.location.href = "/web/loan/page/realName"
            }
          },
          'json');
    } else {
      if ($phone.val() == null || $phone.val() === '') {
        showError("phone","请输入手机号")
      }else if ($loginPassword.val() == null || $loginPassword.val() === '') {
        showError("loginPassword","请输入密码")
      }else if ($messageCode.val() == null || $messageCode.val() === '') {
        showError("messageCode","请输入验证码")
      }
    }
  })


});





