package app.model

import java.util.*


class Invitation(
	var userId: Int,
	var fromUserId: Int,
	var time: Date,
	var message: String,
	var status: Int,
)

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
