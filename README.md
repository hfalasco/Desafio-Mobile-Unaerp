## EXPLICACAO DO DESENVOLVIMENTO ##

Cada aluno deverá desenvolver **individualmente** um **aplicativo Android** com uma proposta coerente, cujas funcionalidades estejam relacionadas a um mesmo problema ou tema.

## Parcial: Android Views (XML) e navegação + intent

### Requisitos obrigatórios
O aplicativo deve:
- Implementar duas telas em Android Views com layouts XML, usando Views (i.e. `TextView`, `ImageView`, `Space`) e ViewGroups (i.e. `LinearLayout`, `FrameLayout`, `RecyclerView`) adequados ao fluxo do aplicativo.
- Implementar a navegação entre as duas telas por meio de `Intent` explícita, com passagem de dados quando aplicável.
- Conectar as Views ao Kotlin por `ViewBinding` ou `findViewById`, tratando pelo menos uma interação que atualize a interface ou avance o fluxo.
- Representar os dados exibidos com modelos imutáveis (`data class`) e tratar corretamente valores opcionais.
- Utilizar dados simulados (_mocks_), sem necessidade de API ou banco de dados nesta etapa.
### Itens opcionais
- Utilizar apenas ViewBinding ao invés de `findViewById`
- Criar componentes XML reutilizáveis, inflá-los quando necessário e tratar eventos que atualizem a interface
- Implementar uma interface funcional em `Fragment`, utilizando seu ciclo de vida e ViewBinding



---

# 🏴‍☠️ Grand Line Store — solução

Aplicativo Android (Kotlin + Views/XML) de uma **loja geek de colecionáveis de One Piece**: figures, mangás, card game e acessórios. O usuário navega pelo catálogo, filtra por categoria e estoque, abre o produto em um **cartaz de recompensa ("WANTED")**, escolhe a quantidade e adiciona à sacola.

> Produtos, preços e estoques são fictícios. Ícones e ilustrações são vetores próprios (sem imagens oficiais da obra).

## Fluxo

```
MainActivity (Catálogo)                       ProductDetailActivity (Cartaz WANTED)
 └─ ProductListFragment                         ├─ recebe EXTRA_PRODUCT_ID
     ├─ chips de categoria + switch de estoque  ├─ QuantityStepperView (− 1 +) → total atualizado
     ├─ RecyclerView de produtos ── clique ───► ├─ "Adicionar à sacola"
     │      (Intent explícita + id do produto)  │
     └─ recebe AddToCartResult ◄── setResult ───┘
         → atualiza sacola, badge e Snackbar
```

## Arquitetura

MVVM com separação em camadas e injeção de dependências manual:

```
com.unaerp.grandlinestore
├── GrandLineStoreApp.kt          # Application que expõe o AppContainer
├── di/AppContainer.kt            # cria as dependências (repositório)
├── domain/                       # Kotlin puro, sem dependências do Android
│   ├── model/                    # Product, Cart, CartItem, ProductFilter, enums (data classes imutáveis)
│   └── repository/               # ProductRepository (interface)
├── data/mock/                    # MockProductDataSource + MockProductRepository
└── ui/
    ├── catalog/                  # MainActivity, ProductListFragment, CatalogViewModel, ProductAdapter
    ├── detail/                   # ProductDetailActivity, ProductDetailViewModel, OpenProductDetailContract
    ├── components/               # QuantityStepperView (custom view) e InfoRow (<include>)
    └── common/                   # formatação de preço, mapeamento domínio → recursos, insets
```

Decisões:
- **Domínio sem Android:** `ProductCategory` não conhece `R.string`/`R.drawable`. O mapeamento fica em `ui/common/ProductUi.kt`, o que deixa o domínio testável com JUnit puro.
- **Dinheiro em centavos (`Long`):** evita erros de arredondamento com `Double`.
- **Validação nos modelos:** os blocos `init { require(...) }` impedem estados inválidos (preço promocional maior que o original, estoque negativo etc.).
- **Navegação encapsulada:** `OpenProductDetailContract` (Activity Result API) concentra a Intent explícita e as chaves dos extras na ida e na volta.
- **Estado que sobrevive à rotação:** `CatalogViewModel` tem escopo de Activity e é compartilhado com o Fragment. No detalhe, a quantidade fica no `SavedStateHandle`.
- **Edge-to-edge:** obrigatório com `targetSdk 35`. Os insets são tratados em `ViewExtensions.kt`.

## Requisitos do enunciado

| Requisito | Onde |
|---|---|
| 2 telas em Views/XML com Views e ViewGroups adequados | `activity_main.xml`, `activity_product_detail.xml` (TextView, ImageView, Space, LinearLayout, FrameLayout, RecyclerView, NestedScrollView) |
| Navegação por **Intent explícita** com passagem de dados | `OpenProductDetailContract.createIntent` (envia o id) e `createResultIntent` (devolve produto e quantidade) |
| Views conectadas ao Kotlin + interação que atualiza a UI | Chips/switch filtram a lista; o stepper recalcula o total; o botão avança o fluxo e atualiza o badge da sacola |
| Modelos imutáveis (`data class`) e valores opcionais | `Product` (`promotionalPriceInCents`, `description`, `character`, `arc` e `rating` são nullable), tratados com `?.let`, `?:` e `setTextOrGone` |
| Dados simulados (mocks) | `data/mock/MockProductDataSource.kt` |
| **Opcional:** apenas ViewBinding | Nenhum `findViewById` no projeto (`buildFeatures.viewBinding = true`) |
| **Opcional:** componentes XML reutilizáveis inflados dinamicamente | `view_category_chip.xml` (inflado por categoria), `view_quantity_stepper.xml` (custom view com atributos próprios), `view_info_row.xml` (`<include>`), `item_product.xml` |
| **Opcional:** Fragment com ciclo de vida e ViewBinding | `ProductListFragment`: binding criado em `onCreateView` e liberado em `onDestroyView`, observação com `viewLifecycleOwner` |

## Como executar

1. Abra a pasta no **Android Studio** (Ladybug ou mais recente) e aguarde o *Gradle Sync*.
2. Execute a configuração `app` em um emulador ou dispositivo com Android 8.0 (API 26) ou superior.
3. Testes unitários: `./gradlew testDebugUnitTest` (ou clique com o botão direito em `app/src/test` → *Run Tests*).

Stack: Kotlin 2.0, AGP 8.7, compile/target SDK 35, min SDK 26, Material 3, AndroidX Lifecycle (ViewModel/LiveData), Fragment KTX, RecyclerView.
