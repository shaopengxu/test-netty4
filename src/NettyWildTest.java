public class NettyWildTest {

	public static void main(String[] args) {

		/**
		 * get a random question / param none / return [questionId,questionContent,[answers]]
		 * get the hot questions / param none / return [questions]
		 * get question votes / param questionId / return [{answerIndex:voteNumber}]
		 * get my question / param none / return [questions]
		 * create a question / param {questionContent,[answers]} / return none
		 * vote a question / param {questionId,answerIndex} / return none
		 * add a choice to a question / param {questionId, answer}
		 * 
		 * 
		 * if one edits the question, all the votes would be cleared.
		 */
	}
}
