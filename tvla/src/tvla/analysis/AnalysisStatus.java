package tvla.analysis;

import tvla.core.TVSFactory;
import tvla.core.generic.AdvancedCoerce;
import tvla.core.generic.GenericHashPartialJoinTVSSet;
import tvla.core.generic.MultiConstraint;
import tvla.transitionSystem.Action;
import tvla.transitionSystem.Location;
import tvla.util.Logger;
import tvla.util.ProgramProperties;
import tvla.util.StringUtils;
import tvla.util.Timer;

/**
 * This class represents the status of an analysis. The status is comprised of
 * statistical information and general mode information which controls the
 * behavior of the analysis (for example, how verbose it should be). The status
 * also provides some measures for analyses to be responsive by receiving input
 * from the user. Currently, the only input that's handled is when a user pushes
 * the enter button, which causes the analysis to (gracefully) finish.
 * 
 * @author Roman Manevich.
 * @since 6.4.2002 Initial creation.
 */
public class AnalysisStatus {
	/**
	 * The id of the timer that measures the time needed to load the
	 * specification.
	 */
	public static final int LOAD_TIME = 0;

	/**
	 * The id of the timer that measures the total analysis time.
	 */
	public static final int TOTAL_ANALYSIS_TIME = 1;

	/**
	 * The id of the timer that measures the time spent updating structures.
	 */
	public static final int UPDATE_TIME = 2;

	/**
	 * The id of the timer that measures the time spent evaluating
	 * precontitions.
	 */
	public static final int PRECONDITION_TIME = 3;

	/**
	 * The id of the timer that measures the time spent blurring structures.
	 */
	public static final int BLUR_TIME = 4;

	/**
	 * The id of the timer that measures the time spent focusing structures.
	 */
	public static final int FOCUS_TIME = 5;

	/**
	 * The id of the timer that measures the time spent coercing structures.
	 */
	public static final int COERCE_TIME = 6;

	/**
	 * The id of the timer that measures the time spent merging structures to
	 * sets.
	 */
	public static final int JOIN_TIME = 7;

	/**
	 * The id of the timer that measures the time spent meeting structures.
	 */
	public static final int MEET_TIME = 8;

	/**
	 * The id of the timer that measures the time spent composing structures.
	 */
	public static final int COMPOSE_TIME = 9;

	/**
	 * The id of the timer that measures the time spent decomposing structures.
	 */
	public static final int DECOMPOSE_TIME = 10;

	/**
	 * The id of the timer that measures the time spent permuting structures.
	 */
	public static final int PERMUTE_TIME = 11;

	/**
	 * The id of the timer that measures the time spent on frame.
	 */
	public static final int FRAME_TIME = 12;

	/**
	 * The id of the timer that measures the time spent on termination analysis
	 */
	public static final int TD_TIME = 13;

	/**
	 * The id of the timer that measures the time spent on termination analysis
	 * summarization section
	 */
	public static final int TD_SUM_TIME = 14;

	/**
	 * The id of the timer that measures the time spent on termination analysis
	 * verification section
	 */
	public static final int TD_SAT_TIME = 15;

	/**
	 * Number of timers
	 */
	public static final int NUM_TIMERS = 16;

	/**
	 * The global load timer.
	 */
	public static Timer loadTimer = new Timer();

	/**
	 * Specifies whether runtime printouts should be printed to the console.
	 */
	public static boolean terse = ProgramProperties.getBooleanProperty("tvla.terse", false);

	/**
	 * Specifies whether warnings should be emitted.
	 */
	public static boolean emitWarnings;

	/**
	 * Specifies whether debug information should be emitted.
	 */
	public static boolean debug;

	/**
	 * The number of structures generated by the analysis.
	 */
	public int numberOfStructures;

	/**
	 * The number of messagess generated by the analysis.
	 */
	public int numberOfMessages;

	/**
	 * The number of structures discarded due to constraint breaches.
	 */
	public int numberOfConstraintBreaches = 0;

	/**
	 * The number of constraint breaches after update
	 */
	public int numberOfConstraintBreachesAfterUpdtae = 0;

	/**
	 * The amount of memory used just before the analysis started.
	 */
	public long initialMemory;

	/**
	 * Enables automatically calling exhaustive Garbage-collection.
	 */
	private static boolean autoGC;

	/**
	 * Statistics printout frequency (every k structures)
	 */
	public int statisticsEvery;

	/**
	 * The rate at which intermediate results should be dumped (every k
	 * structures).
	 */
	public int dumpEvery;

	public boolean continuousStatisticsReports;

	public Boolean TerminationAnalysisResult = null;

	/**
	 * The maximum amount of memory used during analysis.
	 */
	protected long maxMemory;

	/**
	 * The average amount of memory used during analysis.
	 */
	protected long averageMemory;

	/**
	 * The number of times memory was measured during analysis.
	 */
	protected long memorySamples;

	protected int structuresPerSecond;
	protected double loadTime;
	protected double meetTime;
	protected double composeTime;
	protected double decomposeTime;
	protected double permuteTime;
	protected double frameTime;
	protected double totalAnalysisTime = 0;
	protected double updateTime;
	protected double preconditionTime;
	protected double blurTime;
	protected double focusTime;
	protected double coerceTime;
	protected double joinTime;

	protected double tdTime;
	protected double tdSumTime;
	protected double tdSatTime;

	/**
	 * An array of timers.
	 */
	protected Timer[] timers = new Timer[NUM_TIMERS];

	/**
	 * When true, indicates that the analysis should gracefully finish, i.e.,
	 * act as though the fixed-point has been reached.
	 */
	protected boolean finishAnalysis = false;

	/**
	 * The limit for the number of structures generated by the analysis.
	 */
	protected int structuresLimit;

	/**
	 * The limit for the number of messages generated by the analysis.
	 */
	protected int messagesLimit;

	public int numberOfComposeConstraintBreaches = 0;

	/**
	 * Stores the last AnalysisStatus object that was constructued.
	 */
	private static AnalysisStatus activeStatus;

	/**
	 * Constructs an AnalysisStatus object and runs it in a new thread.
	 */
	public AnalysisStatus() {
		debug = ProgramProperties.getBooleanProperty("tvla.debug", false);
		emitWarnings = ProgramProperties.getBooleanProperty("tvla.emitWarnings", true);
		structuresLimit = ProgramProperties.getIntProperty("tvla.engine.maxStructures", -1);
		messagesLimit = ProgramProperties.getIntProperty("tvla.engine.maxMessages", -1);
		dumpEvery = ProgramProperties.getIntProperty("tvla.engine.dumpEvery", -1);
		autoGC = ProgramProperties.getBooleanProperty("tvla.engine.autoGC", autoGC);
		statisticsEvery = ProgramProperties.getIntProperty("tvla.engine.statisticsEvery", 1000);
		continuousStatisticsReports = ProgramProperties.getBooleanProperty("tvla.log.continuousStatisticsReports",
				false);

		initTimers();
		activeStatus = this;
	}

	public static void reset() {
		activeStatus = null;
		loadTimer = new Timer();
	}

	/**
	 * Returns a globaly stored object of the last AnalysisStatus object that
	 * was created.
	 */
	public static AnalysisStatus getActiveStatus() {
		if (activeStatus == null)
			activeStatus = new AnalysisStatus();
		return activeStatus;
	}

	/**
	 * Checks whether there's a reason to (prematurely) finish the analysis. The
	 * reason could be that the user specified a number of structures limit or a
	 * number of messages limit and the limit was reached. Another reason is
	 * when a user explicitly asks for the analysis to finish (by pressing the
	 * keyboard and answering the dialog).
	 */
	public boolean shouldFinishAnalysis() {
		finishAnalysis |= (structuresLimit >= 0 && numberOfStructures > structuresLimit);
		finishAnalysis |= (messagesLimit >= 0 && numberOfMessages > messagesLimit);
		return finishAnalysis;
	}

	/**
	 * Signals for the analysis to finish.
	 */
	public void finishAnalysis() {
		finishAnalysis = true;
	}

	/**
	 * Updates statistics information.
	 */
	public void updateStatus() {
		if (memorySamples == 0) {
			exhaustiveGC();
			initialMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		}

		long currentProfileMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		maxMemory = java.lang.Math.max(currentProfileMemory, maxMemory);
		++memorySamples;
		averageMemory += currentProfileMemory;
		TVSFactory.getInstance().collectStatisticsInfo();
	}

	/**
	 * Starts one of the status timers.
	 * 
	 * @param timerId
	 *            The id of the timer.
	 */
	public boolean startTimer(int timerId) {
		return timers[timerId].start();
	}

	static final double ExhaustiveGCPeriod = 1.0;

	/**
	 * Stops one of the status timers.
	 * 
	 * @param timerId
	 *            The id of the timer.
	 */
	public void stopTimer(int timerId) {
		timers[timerId].stop();

		if (timerId == TOTAL_ANALYSIS_TIME) {
			// update the structurePerSecond figure
			structuresPerSecond = numberOfStructures;
			double totalTime = getTimerMeasure(TOTAL_ANALYSIS_TIME);
			structuresPerSecond = (int) ((double) numberOfStructures / totalTime);

			if (totalTime - totalAnalysisTime > ExhaustiveGCPeriod)
				exhaustiveGC();

			// update time figures
			totalAnalysisTime = getTimerMeasure(TOTAL_ANALYSIS_TIME);
			updateTime = getTimerMeasure(UPDATE_TIME);
			preconditionTime = getTimerMeasure(PRECONDITION_TIME);
			blurTime = getTimerMeasure(BLUR_TIME);
			focusTime = getTimerMeasure(FOCUS_TIME);
			coerceTime = getTimerMeasure(COERCE_TIME);
			joinTime = getTimerMeasure(JOIN_TIME);
			loadTime = getTimerMeasure(LOAD_TIME);
			meetTime = getTimerMeasure(MEET_TIME);
			composeTime = getTimerMeasure(COMPOSE_TIME);
			decomposeTime = getTimerMeasure(DECOMPOSE_TIME);
			permuteTime = getTimerMeasure(PERMUTE_TIME);
			frameTime = getTimerMeasure(FRAME_TIME);
			tdTime = getTimerMeasure(TD_TIME);
			tdSumTime = getTimerMeasure(TD_SUM_TIME);
			tdSatTime = getTimerMeasure(TD_SAT_TIME);
		}
	}

	public void printMemoryStatistics() {
		Logger.println("Memory Statistics. Samples = " + memorySamples);

		Logger.println("Initial memory             : " + initialMemory / (float) 1000000 + "\tMb");
		Logger.println("Max memory                 : " + maxMemory / (float) 1000000 + "\tMb");
		Logger.println("Average memory             : " + (averageMemory / memorySamples) / (float) 1000000 + "\tMb");
	}

	public void resetMemoryStatistics() {
		memorySamples = 0;
		initialMemory = 0;
		maxMemory = 0;
		averageMemory = 0;
	}

	/**
	 * Prints statistics gathered during the analysis to the log stream.
	 * 
	 * @author Roman Manevich.
	 * @since 21.7.2001
	 */
	static final boolean printIncrementStats = ProgramProperties
			.getBooleanProperty("tvla.engine.incremental.printStatistics", false);

	public void printStatistics(StringBuffer to) {
		double currentTotalTime = getTimerMeasure(TOTAL_ANALYSIS_TIME);
		double currentMeetTime = getTimerMeasure(MEET_TIME);
		double currentComposeTime = getTimerMeasure(COMPOSE_TIME);
		double currentDecomposeTime = getTimerMeasure(DECOMPOSE_TIME);
		double currentPermuteTime = getTimerMeasure(PERMUTE_TIME);
		double currentFrameTime = getTimerMeasure(FRAME_TIME);
		double currentLoadTime = getTimerMeasure(LOAD_TIME);
		double currentFocusTime = getTimerMeasure(FOCUS_TIME);
		double currentPreconditionTime = getTimerMeasure(PRECONDITION_TIME);
		double currentUpdateTime = getTimerMeasure(UPDATE_TIME);
		double currentCoerceTime = getTimerMeasure(COERCE_TIME);
		double currentBlurTime = getTimerMeasure(BLUR_TIME);
		double currentJoinTime = getTimerMeasure(JOIN_TIME);

		double currentTdTime = getTimerMeasure(TD_TIME);
		double currentTdSumTime = getTimerMeasure(TD_SUM_TIME);
		double currentTdSatTime = getTimerMeasure(TD_SAT_TIME);

		// Normalize times to reduce the effect of accumulating errors.
		/*
		 * double sumTimes = currentFocusTime + currentPreconditionTime +
		 * currentUpdateTime + currentCoerceTime + currentBlurTime +
		 * currentJoinTime;
		 * 
		 * currentFocusTime = (currentFocusTime/sumTimes) * currentTotalTime;
		 * currentPreconditionTime = (currentPreconditionTime/sumTimes) *
		 * currentTotalTime; currentUpdateTime = (currentUpdateTime/sumTimes) *
		 * currentTotalTime; currentCoerceTime = (currentCoerceTime/sumTimes) *
		 * currentTotalTime; currentBlurTime = (currentBlurTime/sumTimes) *
		 * currentTotalTime; currentJoinTime = (currentJoinTime/sumTimes) *
		 * currentTotalTime;
		 */
		final double PERCISION = 100.0;
		/*
		 * currentLoadTime = ((int)(loadTime*PERCISION))/PERCISION;
		 * currentFocusTime = ((int)(focusTime*PERCISION))/PERCISION;
		 * currentPreconditionTime =
		 * ((int)(preconditionTime*PERCISION))/PERCISION; currentUpdateTime =
		 * ((int)(updateTime*PERCISION))/PERCISION; currentCoerceTime =
		 * ((int)(coerceTime*PERCISION))/PERCISION; currentBlurTime =
		 * ((int)(blurTime*PERCISION))/PERCISION; currentJoinTime =
		 * ((int)(joinTime*PERCISION))/PERCISION;
		 */
		currentLoadTime = ((int) (currentLoadTime * PERCISION)) / PERCISION;
		currentFocusTime = ((int) (currentFocusTime * PERCISION)) / PERCISION;
		currentPreconditionTime = ((int) (currentPreconditionTime * PERCISION)) / PERCISION;
		currentUpdateTime = ((int) (currentUpdateTime * PERCISION)) / PERCISION;
		currentCoerceTime = ((int) (currentCoerceTime * PERCISION)) / PERCISION;
		currentBlurTime = ((int) (currentBlurTime * PERCISION)) / PERCISION;
		currentJoinTime = ((int) (currentJoinTime * PERCISION)) / PERCISION;
		currentMeetTime = ((int) (currentMeetTime * PERCISION)) / PERCISION;
		currentComposeTime = ((int) (currentComposeTime * PERCISION)) / PERCISION;
		currentDecomposeTime = ((int) (currentDecomposeTime * PERCISION)) / PERCISION;
		currentPermuteTime = ((int) (currentPermuteTime * PERCISION)) / PERCISION;
		currentFrameTime = ((int) (currentFrameTime * PERCISION)) / PERCISION;
		currentTdTime = ((int) (currentTdTime * PERCISION)) / PERCISION;
		currentTdSumTime = ((int) (currentTdSumTime * PERCISION)) / PERCISION;
		currentTdSatTime = ((int) (currentTdSatTime * PERCISION)) / PERCISION;

		memorySamples = memorySamples > 0 ? memorySamples : 1;

		exhaustiveGC();

		if (terse)
			Logger.println(StringUtils.addUnderline("Statistics"));

		println(to, "Initial memory             : " + initialMemory / (float) 1000000 + "\tMb");
		println(to, "Max memory                   : " + maxMemory / (float) 1000000 + "\tMb");
		println(to, "Average memory               : " + (averageMemory / memorySamples) / (float) 1000000 + "\tMb");
		println(to, "Load time                    : " + (float) currentLoadTime + "\tseconds");
		println(to, "Total analysis time          : " + (float) currentTotalTime + "\tseconds");
		println(to, "Focus time                   : " + (float) currentFocusTime + "\tseconds");
		println(to, "Precondition time            : " + (float) currentPreconditionTime + "\tseconds");
		println(to, "Update time                  : " + (float) currentUpdateTime + "\tseconds");
		println(to, "Coerce time                  : " + (float) currentCoerceTime + "\tseconds");
		println(to, "Blur time                    : " + (float) currentBlurTime + "\tseconds");
		println(to, "Join time                    : " + (float) currentJoinTime + "\tseconds");
		if (currentMeetTime > 0)
			println(to, "Meet time                    : " + (float) currentMeetTime + "\tseconds");
		if (currentComposeTime > 0)
			println(to, "Compose time                 : " + (float) currentComposeTime + "\tseconds");
		if (currentDecomposeTime > 0)
			println(to, "Decompose time               : " + (float) currentDecomposeTime + "\tseconds");
		if (currentPermuteTime > 0)
			println(to, "Permute time                 : " + (float) currentPermuteTime + "\tseconds");
		if (currentFrameTime > 0)
			println(to, "Frame time                   : " + (float) currentFrameTime + "\tseconds");

		println(to, "Total number of structures : " + numberOfStructures);
		println(to, "Structures per second      : " + structuresPerSecond);
		println(to, "Num of constraint breaches : " + numberOfConstraintBreaches);
		if (numberOfComposeConstraintBreaches != 0)
			println(to, "Num of compose constraint breaches : " + numberOfComposeConstraintBreaches);
		println(to, "Total number of messages   : " + numberOfMessages);
		println(to, "#locations where property failed  : " + Action.locationsWherePropertyFails.size());
		println(to, "locations where property failed  = ");
		for (Location failLoc : Action.locationsWherePropertyFails) {
			println(to, "PROPERTY FAILED at " + failLoc.label());
		}

		if (TerminationAnalysisResult != null) {
			println(to, "");
			println(to, "Termination analysis result  : "
					+ (TerminationAnalysisResult ? "terminating" : "possibly non-terminating"));
			println(to, "Termination analysis time    : " + (float) currentTdTime + "\tseconds");
			println(to, "Summarizaiton time           : " + (float) currentTdSumTime + "\tseconds");
			println(to, "Verification time            : " + (float) currentTdSatTime + "\tseconds");
		}

		println(to, "");
		/*
		 * Logger.println("Eval stats:"); Logger.println(" evals: " +
		 * FormulaIterator.stat_Evals); Logger.println(" non-evals: " +
		 * FormulaIterator.stat_NonEvals); Logger.println(" default: " +
		 * FormulaIterator.stat_DefaultAssigns); Logger.println(" predicate: " +
		 * FormulaIterator.stat_PredicateAssigns); Logger.println(" negation: "
		 * + FormulaIterator.stat_PredicateNegationAssigns);
		 * Logger.println(" equality: " + FormulaIterator.stat_EqualityAssigns);
		 * Logger.println(" quant: " + FormulaIterator.stat_QuantAssigns);
		 * Logger.println(" ---------------------------");
		 */

		// MultiConstraint.printStatistics();
		if (printIncrementStats) {
			println(to, "new    structs: " + GenericHashPartialJoinTVSSet.countNewStructures);
			println(to, "merged structs: " + GenericHashPartialJoinTVSSet.countMergedStructures);
			println(to, "focus efficiency: " + 100 * Engine.stat_FocusDelta + "%");

			println(to, "Coerce stats:");
			println(to, " coerce: " + AdvancedCoerce.stat_coerce);
			println(to,
					" coerce_evals: " + (AdvancedCoerce.stat_incrementalEvals + AdvancedCoerce.stat_goodConstraints));
			println(to, " coerce_inc_evals: " + AdvancedCoerce.stat_incrementalEvals);
			println(to, " coerce_bad_evals: " + AdvancedCoerce.stat_goodConstraints);
			println(to, " coerce_inc_iters: " + (AdvancedCoerce.stat_coerceIncrementalIters
					/ ((float) AdvancedCoerce.stat_incrementalEvals + 1)));
			println(to, " coerce_bad_iters: "
					+ (AdvancedCoerce.stat_coerceBasicIters / ((float) AdvancedCoerce.stat_goodConstraints + 1)));
			println(to, " coerce_firstpass: "
					+ (100L * AdvancedCoerce.stat_firstCoerceCalls / (float) AdvancedCoerce.stat_totalCoerceCalls)
					+ "%");
			println(to, " ---------------------------");
			println(to, " coerce_all  : " + AdvancedCoerce.time_coerce);
			println(to, " coerce_inc  : " + AdvancedCoerce.time_coerceInc);
			println(to, " coerce_full : " + AdvancedCoerce.time_coerceBad);
			println(to, " coerce_loop1: " + AdvancedCoerce.time_coerceLoop1);
			println(to, " coerce_loop2: " + AdvancedCoerce.time_coerceLoop2);
			println(to, " coerce_loop3: " + AdvancedCoerce.time_coerceLoop3);
			println(to, " coerce_tc   : " + AdvancedCoerce.time_coerceTC);
			println(to, " coerce_get  : " + AdvancedCoerce.time_coerceGetIncrements);

			MultiConstraint.printStatistics();
		}
	}

	private void println(StringBuffer sb, String str) {
		sb.append(str);
		sb.append(StringUtils.newLine);
	}

	public void printStatistics() {
		StringBuffer to = new StringBuffer();
		printStatistics(to);
		Logger.println(to);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		printStatistics(sb);
		return sb.toString();
	}

	/**
	 * Returns the time measured by the specified timer.
	 * 
	 * @param timerId
	 *            The id of the timer.
	 */
	public double getTimerMeasure(int timerId) {
		return timers[timerId].total() / 1000.0;
	}

	/**
	 * Does exhaustive garbage-collection until there is no change in the amount
	 * of free memory.
	 */
	public static void exhaustiveGC() {
		if (!autoGC)
			return;

		long free1 = 0;
		long free2 = Runtime.getRuntime().freeMemory();
		while (free1 < free2) {
			free1 = free2;
			System.gc();
			free2 = Runtime.getRuntime().freeMemory();
		}

	}

	/**
	 * Initializes the timers array and stores the load timer at the first
	 * position.
	 */
	protected void initTimers() {
		timers[0] = loadTimer; // reference the static timer
		for (int i = 1; i < timers.length; ++i)
			timers[i] = new Timer();
	}
}