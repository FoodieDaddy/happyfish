/* 工具函数 */
let _ = {
    addClass: function (ele, name) {
        let c = ele.className.split(' ')
        if (c.indexOf(name) !== -1) return
        c.push(name)
        ele.className = c.join(' ')
    },
    removeClass: function (ele, name) {
        let c = ele.className.split(' ')
        let ind = c.indexOf(name)
        if (ind === -1) return
        ele.className = c.slice(0, ind).concat(c.slice(ind + 1)).join(' ')
    },
}

/**
 * @class Game
 * @constructor
 * @param {string} panel 容器classname
 * @param {[NodeElement]} sprites 精灵元素
 * @param {NodeElement} bullet 子弹元素
 * @param {function} boomFn 命中函数
 */
function Game (panel, sprites, bullet, boomFn) {
    this._panel = document.querySelector(panel)
    this._sprites = sprites
    this._bullet = bullet

    this._playing = false
    this._store = {
        _colp: {
            state: false, // 是否有碰撞到元素
            target: null, // 碰撞到的元素
        },
    }

    this._boomFn = boomFn

    return this
}

Game.prototype.reset = function () {
    // 1. 重置鱼的位置
    if (this._store._colp.target) {
        _.removeClass(this._store._colp.target, 'catching')
        _.removeClass(this._store._colp.target, 'wave-stop')

        this._store._colp.state = false
        this._store._colp.target = null
    }

    // 2. 鱼重新开始运动
    this._sprites.forEach((ele) => {
        _.addClass(ele, 'swimming')
        _.removeClass(ele, 'stop')
    })

    // 3 重置子弹的位置
    _.removeClass(this._bullet, 'go')
    _.removeClass(this._bullet, 'aim')
}

Game.prototype.start = function () {
    if (this._playing) return
    // 发射子弹
    _.addClass(this._bullet, 'go')
    // this._bullet.style.animation = 'bulletRoutine 2s linear forwards'

    this._playing = true
    this._frameFn()
}

Game.prototype._stop = function () {
    this._playing = false
}

Game.prototype._frameFn = function () {
    requestAnimationFrame(() => {
        // 1. 检查所有元素的位置 并更新
        // this._checkXY()

        // 2. 检测碰撞
        this._collpase()

        // 3. 有碰撞则停止所有的动画 -> 播放击中动画特效
        if (this._store._colp.state) {
            this._update()
            this._stop()
        } else {
            this._frameFn()
        }
    })
}

Game.prototype._collpase = function () {
    let bPos = this._bullet.getBoundingClientRect()
    for (let i=0, len=this._sprites.length; i<len; i++) {
        let target = this._sprites[i]
        // 1. 检测碰撞
        let tPos = target.getBoundingClientRect()
        // 2. 未碰撞到
        if ( (bPos.top > (tPos.top + tPos.height))
            || bPos.left > (tPos.left + tPos.width)
            || (bPos.left + bPos.width) < tPos.left ) {
            this._store._colp.state = false
            continue
        }

        // 3. 碰撞到
        this._store._colp.state = true
        this._store._colp.target = target
        return
    }
}

// 动画被击中
Game.prototype._update = function () {
    // 1. bullet boom
    _.addClass(this._bullet, 'aim')

    // 2. stop animation
    // document.querySelector('.line-1').style.animationPlayState = 'paused'
    // this._fishPool.style.animationPlayState = 'paused'
    this._sprites.forEach((ele) => {
        _.addClass(ele, 'stop')
    })

    // 3. fish catch
    _.addClass(this._store._colp.target, 'catching wave-stop')

    /* ************** */
    /* 这里可以写中奖的逻辑: 可以单独抽出来成一个传参函数 new Game中写 如果需要参数可以放在dom data-set上 */
    setTimeout(() => {
        this._boomFn(this._store._colp.target)
    }, 600)
    /* ************** */
}

if (typeof module !== 'undefined') {
    // module.exports = Game
}