<template>
  <div>
    <v-dialog v-model="dialog" max-width="600">
      <v-card>
        <v-card-title>
          {{ modoEdicao ? "Editar Produto" : "Visualizar Produto" }}
        </v-card-title>
        <v-card-text>
          <v-container>
            <!-- Campos para exibir/entrar com os detalhes do produto -->
            <v-form ref="form">
              <v-row>
                <v-col cols="12">
                  <v-text-field
                    v-model="value.nome"
                    label="Nome"
                    :rules="[v => !!v || 'Nome é obrigatório', v => (v && v.length >= 3 && v.length <= 255 ) || 'Nome deve ter entre 3 e 255 caracteres']"
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="12">
                  <v-textarea
                    v-model="value.descricao"
                    label="Descrição"
                    :rules="[v => !!v || 'Descrição é obrigatório', v => (v && v.length >= 3 && v.length <= 255 ) || 'Descrição deve ter entre 3 e 2000 caracteres']"
                  ></v-textarea>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="12">
                  <v-text-field
                    v-model="value.preco"
                    label="Preço"
                    :rules="[v => !!v || 'O preço é obrigatório', v => (/[0-9]+(?:\.[0-9]+)/).test(v) || 'O preço deve ter o formato 1000.0']"
                  ></v-text-field>
                </v-col>
              </v-row>
            </v-form>

            <!-- Adicione mais campos conforme necessário -->
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="dialog = false"
            >Cancelar</v-btn
          >
          <v-btn color="blue darken-1" text @click="salvarProduto"
            >Salvar</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import BuscaProdutoDTO from "@/models/BuscaProdutoDTO";
import { format, unformat } from "v-money3";

export default defineComponent({
  props: {
    modelValue: { type: BuscaProdutoDTO, required: true }, // Propriedade para receber o objeto do produto
    modoEdicao: Boolean, // Propriedade para definir se estamos no modo de edição
    aberto: { type: Boolean, default: false },
  },
  data() {
    return {
      config: {
        prefix: "R$",
        suffix: "",
        thousands: ",",
        decimal: ".",
        precision: 2,
        disableNegative: true,
        min: 0.01,
        max: 999999999,
        allowBlank: false,
        minimumNumberOfCharacters: 0,
        shouldRound: true,
        focusOnRight: false,
        modelModifiers: {
          number: true,
        },
      },
    };
  },
  computed: {
    value: {
      get() {
        return this.modelValue;
      },
      set(value) {
        this.$emit("update:modelValue", value);
      },
    },
    dialog: {
      get() {
        return this.aberto;
      },
      set(value) {
        this.$emit("update:aberto", value);
      },
    },
  },
  methods: {
    async salvarProduto() {
        const res =  await this.$refs.form.validate()
        if(res.valid)
          this.$emit("salvar", this.modelValue);
    },
  },
});
</script>
