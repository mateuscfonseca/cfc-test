// ProdutosService.ts

import BuscaProdutoDTO from "@/models/BuscaProdutoDTO";
import api from "./api";

class ProdutosService {
  async total(): Promise<number> {
    try {
      const response = await api.get("/produtos/total");
      return response.data; // Retorna os dados dos produtos
    } catch (error) {
      console.error("Erro ao buscar produtos:", error);
      throw error; // Lança o erro para tratamento posterior
    }
  }
  async listarProdutos(page = 1, pageSize = 10): Promise<BuscaProdutoDTO[]> {
    try {
      const response = await api.get("/produtos", {
        params: { page, pageSize },
      });
      return response.data.map((data: any) => BuscaProdutoDTO.fromJSON(data)); // Retorna os dados dos produtos
    } catch (error) {
      console.error("Erro ao buscar produtos:", error);
      throw error; // Lança o erro para tratamento posterior
    }
  }
  async criar(item: BuscaProdutoDTO): Promise<BuscaProdutoDTO> {
    try {
      const response = await api.post("/produtos", item);
      return BuscaProdutoDTO.fromJSON(response.data); // Retorna os dados dos produtos
    } catch (error) {
        throw error.response.data
    }
  }
  async atualizar(item: BuscaProdutoDTO | null): Promise<BuscaProdutoDTO> {
    try {
      const response = await api.put("/produtos", item);
      return BuscaProdutoDTO.fromJSON(response.data); // Retorna os dados dos produtos
    } catch (error) {
      throw error.response.data; // Lança o erro para tratamento posterior
    }
  }
  async deletar(item: BuscaProdutoDTO | null): Promise<null> {
    try {
      const response = await api.delete("/produtos/" + item!.id);
      return null // Retorna os dados dos produtos
    } catch (error) {
      console.error("Erro ao buscar produtos:", error);
      throw error; // Lança o erro para tratamento posterior
    }
  }
}

export default new ProdutosService();
