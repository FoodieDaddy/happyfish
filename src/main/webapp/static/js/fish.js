$(function(){

	//返回主页
	$('.h-back').click(function(){
		window.location.href="/static/index.html"
	})
	//点击规则
	$(".h-rule").click(function(){
		$('.fish-rule').show();
	})

	$(".fish-rule-close").click(function(){
		$('.fish-rule').hide();
	})
	//点击金币显示充值页面
	$('.h-money').click(function(){
		$('.pay').show()
	})
	//充值数字按钮
	$('.pay-list li').click(function(){
		$('.pay-list li').css('background','url(./img/btn_pay2.png) no-repeat').css('background-size','100% 100%');
		$(this).css('background','url(./img/btn_pay1.png) no-repeat').css('background-size','100% 100%');
	})

	//支付关闭
	$('.pay-close').click(function(){
		$('.pay').hide();
		$('.list li').find('.active2').removeClass('active')
		$('.list li').find('.no-active').removeClass('no-active1')
		$('.list li').eq(0).find('.active2').addClass('active')
		$('.list li').eq(0).find('.no-active').addClass('no-active1')
	})

	


	//切换为高倍场
	let l_h_num = 1;
	$('.h-btn').click(function(){
		 l_h_num = 2;
		$('.low-btn').show();
		$('.h-btn').hide();
		$('.low-num').hide();
		$('.high-num').show();
		$('.h_bg').css("background",'url(./img/high_bg.jpg) no-repeat').css('background-size','100% 100%');
		$('.paotai2').find('img').attr('src','./img/50x.png');
		$(".high-num li").find('.img-50').attr('src','./img/50x1.png');
		$(".high-num li").find('.img-100').attr('src','./img/100x.png');
		$(".high-num li").find('.img-200').attr('src','./img/200x.png');
		$(".high-num li").removeClass('h-active');
		$(".high-num li").eq(2).addClass('h-active');
		$('.fish-i').each(function(index,item){
			if(index==0){
				$(this).attr('data-val',60);
				$(this).find('.hongbao').text(60);
			}

			if(index==1){
				$(this).attr('data-val',100);
				$(this).find('.hongbao').text(100);
			}

			if(index==2){
				$(this).attr('data-val',150);
				$(this).find('.hongbao').text(150);
			}

			if(index==3){
				$(this).attr('data-val',200);
				$(this).find('.hongbao').text(200);
			}

			if(index==4){
				$(this).attr('data-val','?');
				$(this).find('.hongbao').text('?');
			}
			
		})
	})

	//切换为低倍场
	$('.low-btn').click(function(){
		 l_h_num = 1;
		$('.low-btn').hide();
		$('.h-btn').show();
		$('.low-num').show();
		$('.high-num').hide();
		$('.h_bg').css("background",'url(./img/low_bg.jpg) no-repeat').css('background-size','100% 100%');
		$('.paotai2').find('img').attr('src','./img/5.png')
		$(".low-num li").find('.img-5').attr('src','./img/5b.png');
		$(".low-num li").find('.img-10').attr('src','./img/10.png');
		$(".low-num li").find('.img-20').attr('src','./img/20.png');
		$(".low-num li").removeClass('h-active');
		$(".low-num li").eq(2).addClass('h-active');
		$('.fish-i').each(function(index,item){
			if(index==0){
				$(this).attr('data-val',6);
				$(this).find('.hongbao').text(6);
			}

			if(index==1){
				$(this).attr('data-val',10);
				$(this).find('.hongbao').text(10);
			}

			if(index==2){
				$(this).attr('data-val',15);
				$(this).find('.hongbao').text(15);
			}

			if(index==3){
				$(this).attr('data-val',20);
				$(this).find('.hongbao').text(20);
			}

			if(index==4){
				$(this).attr('data-val','?');
				$(this).find('.hongbao').text('?');
			}
			
		})
	})

	//减按钮
	$('.emp-reduce').click(function(){
		if(l_h_num==1){
			let a_num = 5;
			$(".low-num li").each(function(index,item){
				if($(this).hasClass('h-active')){
					a_num = $(this).attr('a');
				}
			})
			if(a_num==5){
				return;
			}else if(a_num==10){
				$('.paotai2').find('img').attr('src','./img/5.png')
				$(".low-num li").find('.img-5').attr('src','./img/5b.png');
				$(".low-num li").find('.img-10').attr('src','./img/10.png');
				$(".low-num li").find('.img-20').attr('src','./img/20.png');
				$(".low-num li").removeClass('h-active');
				$(".low-num li").eq(2).addClass('h-active');
			}else if(a_num==20){
				$('.paotai2').find('img').attr('src','./img/10.png')
				$(".low-num li").find('.img-5').attr('src','./img/5.png');
				$(".low-num li").find('.img-10').attr('src','./img/10b.png');
				$(".low-num li").find('.img-20').attr('src','./img/20.png');
				$(".low-num li").removeClass('h-active');
				$(".low-num li").eq(1).addClass('h-active');
			}
		}

		if(l_h_num==2){
			let h_num = 50;
			$(".high-num li").each(function(index,item){
				if($(this).hasClass('h-active')){
					h_num = $(this).attr('a');
				}
			})
			if(h_num==50){
				return;
			}else if(h_num==100){
				$('.paotai2').find('img').attr('src','./img/50x.png')
				$(".high-num li").find('.img-50').attr('src','./img/50x1.png');
				$(".high-num li").find('.img-100').attr('src','./img/100x.png');
				$(".high-num li").find('.img-200').attr('src','./img/200x.png');
				$(".high-num li").removeClass('h-active');
				$(".high-num li").eq(2).addClass('h-active');
			}else if(h_num==200){
				$('.paotai2').find('img').attr('src','./img/100x.png')
				$(".high-num li").find('.img-50').attr('src','./img/50x.png');
				$(".high-num li").find('.img-100').attr('src','./img/100x1.png');
				$(".high-num li").find('.img-200').attr('src','./img/200x.png');
				$(".high-num li").removeClass('h-active');
				$(".high-num li").eq(1).addClass('h-active');
			}
		}

	})

	//增加按钮
	$('.emp-add').click(function(){
		if(l_h_num==1){
			let l_num = 5;
			$(".low-num li").each(function(index,item){
				if($(this).hasClass('h-active')){
					l_num = $(this).attr('a');
				}
			})
			if(l_num==20){
				return;
			}else if(l_num==10){
				$('.paotai2').find('img').attr('src','./img/20.png')
				$(".low-num li").find('.img-5').attr('src','./img/5.png');
				$(".low-num li").find('.img-10').attr('src','./img/10.png');
				$(".low-num li").find('.img-20').attr('src','./img/20b.png');
				$(".low-num li").removeClass('h-active');
				$(".low-num li").eq(0).addClass('h-active');
			}else if(l_num==5){
				$('.paotai2').find('img').attr('src','./img/10b.png')
				$(".low-num li").find('.img-5').attr('src','./img/5.png');
				$(".low-num li").find('.img-10').attr('src','./img/10b.png');
				$(".low-num li").find('.img-20').attr('src','./img/20.png');
				$(".low-num li").removeClass('h-active');
				$(".low-num li").eq(1).addClass('h-active');
			}
		}

		if(l_h_num==2){
			let h_add_num = 50;
			$(".high-num li").each(function(index,item){
				if($(this).hasClass('h-active')){
					h_add_num = $(this).attr('a');
				}
			})
			if(h_add_num==200){
				return;
			}else if(h_add_num==100){
				$('.paotai2').find('img').attr('src','./img/200x.png')
				$(".high-num li").find('.img-50').attr('src','./img/50x.png');
				$(".high-num li").find('.img-100').attr('src','./img/100x.png');
				$(".high-num li").find('.img-200').attr('src','./img/200x1.png');
				$(".high-num li").removeClass('h-active');
				$(".high-num li").eq(0).addClass('h-active');
			}else if(h_add_num==50){
				$('.paotai2').find('img').attr('src','./img/100x.png')
				$(".high-num li").find('.img-50').attr('src','./img/50x.png');
				$(".high-num li").find('.img-100').attr('src','./img/100x1.png');
				$(".high-num li").find('.img-200').attr('src','./img/200x.png');
				$(".high-num li").removeClass('h-active');
				$(".high-num li").eq(1).addClass('h-active');
			}
		}

	})

})