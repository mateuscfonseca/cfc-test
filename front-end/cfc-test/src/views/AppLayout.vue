<template>
  <v-app>
    <!-- Cabeçalho -->
    <v-app-bar app color="primary">
      <v-toolbar-title>Minha Aplicação</v-toolbar-title>
      <!-- Adicione qualquer coisa que você queira no cabeçalho, como botões ou menus -->
    </v-app-bar>

    <v-main>
      <v-container fluid>
        <v-row>
          <!-- Barra lateral -->
          <v-col cols="2">
            <v-navigation-drawer app>
              <v-list dense>
                <v-list-item :to="{ name: 'produtos' }">
                  <template v-slot:prepend>
                    <v-icon icon='mdi-view-list' />
                  </template>
                  <v-list-item-title>Produtos</v-list-item-title>
                </v-list-item>
                <!-- Adicione mais itens de menu conforme necessário -->
              </v-list>
            </v-navigation-drawer>
          </v-col>

          <!-- Conteúdo principal -->
          <v-col>
            <router-view></router-view>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import { defineComponent } from "vue";

export default defineComponent({
  name: "AppLayout",
});
</script>

<style scoped>
/* Estilos personalizados para o layout, se necessário */
</style>
