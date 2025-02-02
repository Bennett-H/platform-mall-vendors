(function (i, C) {
  function m (a) {
    return typeof a === 'number'
  } function n (a) {
    return a !== D && a !== null
  } var D, p, r, s = i.Chart, t = i.extend, z = i.each; r = ['path', 'rect', 'circle']; p = {top: 0, left: 0, center: 0.5, middle: 0.5, bottom: 1, right: 1}; var u = C.inArray, A = i.merge, B = function () {
      this.init.apply(this, arguments)
    }; B.prototype = {init: function (a, d) {
      var c = d.shape && d.shape.type; this.chart = a; var b, f; f = {xAxis: 0, yAxis: 0, title: {style: {}, text: '', x: 0, y: 0}, shape: {params: {stroke: '#000000', fill: 'transparent', strokeWidth: 2}}}; b = {circle: {params: {x: 0,
        y: 0}}}; if (b[c]) {
          f.shape = A(f.shape, b[c])
        } this.options = A({}, f, d)
    },
      render: function (a) {
        var d = this.chart, c = this.chart.renderer, b = this.group, f = this.title, e = this.shape, h = this.options, i = h.title, l = h.shape; if (!b) {
          b = this.group = c.g()
        } if (!e && l && u(l.type, r) !== -1) {
          e = this.shape = c[h.shape.type](l.params), e.add(b)
        } if (!f && i) {
         f = this.title = c.label(i), f.add(b)
       }b.add(d.annotations.group); this.linkObjects(); a !== !1 && this.redraw()
      },
      redraw: function () {
        var a = this.options, d = this.chart, c = this.group, b = this.title, f = this.shape, e = this.linkedObject,
          h = d.xAxis[a.xAxis], v = d.yAxis[a.yAxis], l = a.width, w = a.height, x = p[a.anchorY], y = p[a.anchorX], j, o, g, q; if (e) {
            j = e instanceof i.Point ? 'point' : e instanceof i.Series ? 'series' : null, j === 'point' ? (a.xValue = e.x, a.yValue = e.y, o = e.series) : j === 'series' && (o = e), c.visibility !== o.group.visibility && c.attr({visibility: o.group.visibility})
          }e = n(a.xValue) ? h.toPixels(a.xValue + h.minPointOffset) - h.minPixelPadding : a.x; j = n(a.yValue) ? v.toPixels(a.yValue) : a.y; if (!isNaN(e) && !isNaN(j) && m(e) && m(j)) {
           b && (b.attr(a.title), b.css(a.title.style))
           if (f) {
            b = t({}, a.shape.params); if (a.units === 'values') {
              for (g in b) {
              u(g, ['width', 'x']) > -1 ? b[g] = h.translate(b[g]) : u(g, ['height', 'y']) > -1 && (b[g] = v.translate(b[g]))
            }b.width && (b.width -= h.toPixels(0) - h.left); b.x && (b.x += h.minPixelPadding); if (a.shape.type === 'path') {
            g = b.d; o = e; for (var r = j, s = g.length, k = 0; k < s;) {
          typeof g[k] === 'number' && typeof g[k + 1] === 'number' ? (g[k] = h.toPixels(g[k]) - o, g[k + 1] = v.toPixels(g[k + 1]) - r, k += 2) : k += 1
        }
          }
            }a.shape.type === 'circle' && (b.x += b.r, b.y += b.r); f.attr(b)
          }c.bBox = null; if (!m(l)) {
            q = c.getBBox(), l =
q.width
          } if (!m(w)) {
          q || (q = c.getBBox()), w = q.height
        } if (!m(y)) {
        y = p.center
      } if (!m(x)) {
    x = p.center
  }e -= l * y; j -= w * x; d.animation && n(c.translateX) && n(c.translateY) ? c.animate({translateX: e, translateY: j}) : c.translate(e, j)
         }
      },
      destroy: function () {
        var a = this, d = this.chart.annotations.allItems, c = d.indexOf(a); c > -1 && d.splice(c, 1); z(['title', 'shape', 'group'], function (b) {
          a[b] && (a[b].destroy(), a[b] = null)
        }); a.group = a.title = a.shape = a.chart = a.options = null
      },
      update: function (a, d) {
        t(this.options, a); this.linkObjects(); this.render(d)
      },
      linkObjects: function () {
        var a = this.chart, d = this.linkedObject, c = d && (d.id || d.options.id), b = this.options.linkedTo; if (n(b)) {
          if (!n(d) || b !== c) {
            this.linkedObject = a.get(b)
          }
        } else {
          this.linkedObject = null
        }
      }}; t(s.prototype, {annotations: {add: function (a, d) {
        var c = this.allItems, b = this.chart, f, e; Object.prototype.toString.call(a) === '[object Array]' || (a = [a]); for (e = a.length; e--;) {
          f = new B(b, a[e]), c.push(f), f.render(d)
        }
      },
        redraw: function () {
          z(this.allItems, function (a) {
           a.redraw()
         })
        }}}); s.prototype.callbacks.push(function (a) {
         var d =
a.options.annotations, c; c = a.renderer.g('annotations'); c.attr({zIndex: 7}); c.add(); a.annotations.allItems = []; a.annotations.chart = a; a.annotations.group = c; Object.prototype.toString.call(d) === '[object Array]' && d.length > 0 && a.annotations.add(a.options.annotations); i.addEvent(a, 'redraw', function () {
  a.annotations.redraw()
})
       })
})(Highcharts, HighchartsAdapter)
