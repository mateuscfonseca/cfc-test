<template>
  <v-container>
    <h1>Lista de Produtos</h1>
    <v-btn class="mb-2" color="primary" dark @click="adicionarProduto">
      Adicionar
    </v-btn>
    <v-data-table-server
      v-model:items-per-page="itemsPerPage"
      :headers="headers"
      :items="produtos"
      :items-length="totalItems"
      :page="page"
      :loading="loading"
      @update:options="buscarProdutos"
    >
      <template v-slot:[`item.actions`]="{ item }">
        <v-icon class="me-2" size="small" @click="abrirModal(item)">
          mdi-pencil
        </v-icon>
        <v-icon size="small" @click="confirmarDeletarProduto(item)"> mdi-delete </v-icon>
      </template>
    </v-data-table-server>

    <ProdutoComponent
      v-if="produtoSelecionado !== null"
      v-model="produtoSelecionado"
      :modoEdicao="modoEdicao"
      :aberto="produtoSelecionado !== null"
      @salvar="salvarProduto"
      @update:aberto="produtoSelecionado = null"
    />

    <v-dialog v-model="erroDialog" max-width="600">
      <v-card>
        <v-card-title>
          Ocorreu um erro
        </v-card-title>
        <v-card-text>
          <v-container>
            <ul>
            <!-- Exibir cada mensagem de erro em uma lista -->
            <li v-for="mensagem in erros" :key="mensagem">{{ mensagem }}</li>
          </ul>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" @click="erroDialog = false"
            >Fechar</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="confirmarDelecaoDialog" max-width="600">
      <v-card>
        <v-card-title>
          Confirmação
        </v-card-title>
        <v-card-text>
            Você tem certeza que quer excluir o item {{produtoDeletar.nome}}, essa ação não tem retorno.
        </v-card-text>
        <v-card-actions>
          <v-btn color="blue darken-1" @click="deletarProduto(produtoDeletar);confirmarDelecaoDialog=false"
            >Sim</v-btn
          >
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" @click="confirmarDelecaoDialog = false"
            >Não</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>

  </v-container>
</template>

<script lang="ts">
import ProdutoComponent from "@/components/ProdutoComponent.vue";
import BuscaProdutoDTO from "@/models/BuscaProdutoDTO";
import ProdutosService from "@/services/ProdutosService";
import { defineComponent } from "vue";

export default defineComponent({
  name: "ProdutosComponent",
  components: {
    ProdutoComponent,
  },
  data() {
    const produtos: BuscaProdutoDTO[] = [];
    return {
      erroDialog: false,
      confirmarDelecaoDialog: false,
      erros: [],
      produtos,
      itemsPerPage: 10,
      page: 1,
      headers: [
        { title: "Nome", align: "start", sortable: true, key: "nome" },
        {
          title: "Descrição",
          align: "start",
          sortable: true,
          key: "descricao",
        },
        { title: "Preço", align: "start", sortable: true, key: "preco" },
        { title: "Ações", sortable: false, key: "actions" },
      ],
      totalItems: 10,
      loading: true,
      produtoSelecionado: null as BuscaProdutoDTO | null,
      produtoDeletar: null as BuscaProdutoDTO | null,
      modoEdicao: false,
    };
  },
  methods: {
    abrirModal(item: BuscaProdutoDTO) {
      this.produtoSelecionado = item;
      this.modoEdicao = true;
    },
    salvarProduto() {
      if (this.modoEdicao) {
        ProdutosService.atualizar(this.produtoSelecionado).then((res) => {
          this.produtoSelecionado = null;
          this.buscarProdutos();
        }).catch(err => {
          this.erros = err.split('\n').filter(a => !!a)
          this.erroDialog = true;
        });
      } else {
        ProdutosService.criar(this.produtoSelecionado).then((res) => {
          this.produtoSelecionado = null;
          this.buscarProdutos();
        }).catch(err => {
          this.erros = err.split('\n').filter(a => !!a)
          this.erroDialog = true;
        });
      }
    },
    adicionarProduto() {
      this.produtoSelecionado = new BuscaProdutoDTO(null, null, null, null);
      this.modoEdicao = false;
    },
    confirmarDeletarProduto(item: BuscaProdutoDTO) {
      this.produtoDeletar = item;
      this.confirmarDelecaoDialog = true;
    },
    deletarProduto(item: BuscaProdutoDTO) {
      ProdutosService.deletar(item).then((res) => {
        this.buscarProdutos();
      });
    },
    buscarProdutos() {
      this.loading = true;
      ProdutosService.listarProdutos(this.page || 1, this.itemsPerPage || 10)
        .then((produtos) => {
          this.produtos = produtos;
          this.loading = false;
          this.buscarTotalProdutos()
        })
        .catch(console.error)
        .finally(() => (this.loading = false));
    },
    buscarTotalProdutos() {
      ProdutosService.total()
        .then((total) => {
          this.totalItems = total;
        })
        .catch(console.error);
    },
  },
  mounted() {
    this.buscarProdutos();
    this.buscarTotalProdutos();
  },
});
</script>

<style scoped>
/* Estilos personalizados para a página de produtos, se necessário */
</style>
