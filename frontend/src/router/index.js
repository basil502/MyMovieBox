import { createRouter, createWebHistory } from "vue-router";

import Register from "../views/Register.vue";
import Login from "../views/Login.vue";
import MovieList from "../views/MovieList.vue";
import ToWatch from "../views/ToWatch.vue";
import Review from "../views/Review.vue"
import CompletedMovie from "../views/CompletedMovie.vue"

const routes = [
    {path:'/',component:Login},
    {path:'/login',component:Login},
    {path:'/movielist',component:MovieList},
    {path:'/watch',component:ToWatch},
    {path:'/completed',component:CompletedMovie},
    {path:'/register',component:Register},
    {path:'/review',component:Review}
];
const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router