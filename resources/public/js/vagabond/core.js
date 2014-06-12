// Compiled by ClojureScript 0.0-2202
goog.provide('vagabond.core');
goog.require('cljs.core');
goog.require('om.dom');
goog.require('om.dom');
goog.require('om.core');
goog.require('om.core');
cljs.core.enable_console_print_BANG_.call(null);
vagabond.core.root = document.querySelector("#root");
vagabond.core.app_state = cljs.core.atom.call(null,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"text","text",1017460895),"hi"], null));
om.core.root.call(null,(function (app,owner){if(typeof vagabond.core.t6909 !== 'undefined')
{} else
{
/**
* @constructor
*/
vagabond.core.t6909 = (function (owner,app,meta6910){
this.owner = owner;
this.app = app;
this.meta6910 = meta6910;
this.cljs$lang$protocol_mask$partition1$ = 0;
this.cljs$lang$protocol_mask$partition0$ = 393216;
})
vagabond.core.t6909.cljs$lang$type = true;
vagabond.core.t6909.cljs$lang$ctorStr = "vagabond.core/t6909";
vagabond.core.t6909.cljs$lang$ctorPrWriter = (function (this__4048__auto__,writer__4049__auto__,opt__4050__auto__){return cljs.core._write.call(null,writer__4049__auto__,"vagabond.core/t6909");
});
vagabond.core.t6909.prototype.om$core$IRender$ = true;
vagabond.core.t6909.prototype.om$core$IRender$render$arity$1 = (function (this__5021__auto__){var self__ = this;
var this__5021__auto____$1 = this;return React.DOM.h2(null,new cljs.core.Keyword(null,"text","text",1017460895).cljs$core$IFn$_invoke$arity$1(self__.app));
});
vagabond.core.t6909.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_6911){var self__ = this;
var _6911__$1 = this;return self__.meta6910;
});
vagabond.core.t6909.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_6911,meta6910__$1){var self__ = this;
var _6911__$1 = this;return (new vagabond.core.t6909(self__.owner,self__.app,meta6910__$1));
});
vagabond.core.__GT_t6909 = (function __GT_t6909(owner__$1,app__$1,meta6910){return (new vagabond.core.t6909(owner__$1,app__$1,meta6910));
});
}
return (new vagabond.core.t6909(owner,app,null));
}),vagabond.core.app_state,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"target","target",4427965699),vagabond.core.root], null));

//# sourceMappingURL=core.js.map