export default class BuscaProdutoDTO {
  id: number;
  nome: string;
  descricao: string;
  preco: number;

  static fromJSON(json: any): BuscaProdutoDTO {
    const { id, nome, descricao, preco } = json;
    return new BuscaProdutoDTO(id, nome, descricao, preco);
  }

  constructor(id: number, nome: string, descricao: string, preco: number) {
    this.id = id;
    this.nome = nome;
    this.descricao = descricao;
    this.preco = preco;
  }
}
