package br.unifor.fintrack.domain.model

enum class TransactionCategory(
    val displayName: String,
    val applicableTo: Set<TransactionType>
) {
    HOUSING("Moradia", setOf(TransactionType.EXPENSE)),
    FOOD("Alimentação", setOf(TransactionType.EXPENSE)),
    TRANSPORTATION("Transporte", setOf(TransactionType.EXPENSE)),
    ENTERTAINMENT("Lazer", setOf(TransactionType.EXPENSE)),
    HEALTH("Saúde", setOf(TransactionType.EXPENSE)),
    EDUCATION("Educação", setOf(TransactionType.EXPENSE)),
    SHOPPING("Compras", setOf(TransactionType.EXPENSE)),
    SALARY("Salário", setOf(TransactionType.INCOME)),
    FREELANCE("Freelance", setOf(TransactionType.INCOME)),
    INVESTMENT("Investimentos", setOf(TransactionType.INCOME)),
    OTHER("Outros", setOf(TransactionType.EXPENSE, TransactionType.INCOME));

    companion object {
        fun forType(type: TransactionType): List<TransactionCategory> = entries.filter { type in it.applicableTo }
    }
}