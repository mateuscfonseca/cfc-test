import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import Layout from "@/views/AppLayout.vue";
import Produtos from "@/components/ProdutosComponent.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "layout",
    component: Layout,
    children: [
      {
        path: "produtos",
        name: "produtos",
        component: Produtos,
      },
      // Adicione mais rotas conforme necessário
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
