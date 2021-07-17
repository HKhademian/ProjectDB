package app.model

sealed class MyNetwork {
	abstract val user: User
	abstract val mutualCount: Int

	data class MayKnowNetwork(
		override val user: User,
		override val mutualCount: Int,
	) : MyNetwork()

	data class RequestedNetwork(
		override val user: User,
		override val mutualCount: Int,
	) : MyNetwork()

	data class InvitedNetwork(
		override val user: User,
		override val mutualCount: Int,
	) : MyNetwork()
}
