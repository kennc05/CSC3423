import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;


import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static io.jenetics.engine.EvolutionResult.toBestPhenotype;
import io.jenetics.stat.*;

import io.jenetics.DoubleGene;
import io.jenetics.SinglePointCrossover;
import io.jenetics.UniformCrossover;
import io.jenetics.MeanAlterer;
import io.jenetics.TournamentSelector;
import io.jenetics.EliteSelector;
import io.jenetics.Mutator;
import io.jenetics.GaussianMutator;
import io.jenetics.Optimize;
import io.jenetics.Phenotype;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.util.DoubleRange;

public class CW1_GA {
	private static final double A = 10;
	private static final double R = 5.12;
	private static final int N = 10;

	
	/* default parameters for genetic algorithm 
	private int popSize = 1000;
	private int numSurvivors = 1;
	private int tournamentSize = 2;
	private double probMutation = 0.01;
	private double probCrossover = 0.30;
	private int numIters = 1000;
	*/
	
	
	
	//parameters for genetic algorithm 
	private static int popSize;
	private static int numSurvivors;
	private static int tournamentSize;
	private static double probMutation;
	private static double probCrossover;
	private static int numIters;


	
	private static double fitness(final double[] x) {
		double value = A*N;
		for (int i = 0; i < N; ++i) {
			value += x[i]*x[i] - A*cos(2.0*PI*x[i]);
		}

		return value;
	}

	public void parseParams(String paramFile) {
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(paramFile));

			Enumeration enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);
	
				if(key.equals("popSize")) {
					popSize = Integer.parseInt(value);
				} else if(key.equals("numSurvivors")) {
					numSurvivors = Integer.parseInt(value);
				} else if(key.equals("tournamentSize")) {
					tournamentSize = Integer.parseInt(value);
				} else if(key.equals("probMutation")) {
					probMutation = Double.parseDouble(value);
				} else if(key.equals("probCrossover")) {
					probCrossover = Double.parseDouble(value);
				} else if(key.equals("numIters")) {
					numIters = Integer.parseInt(value);
				} else {
					System.out.println("Unknown parameter "+key);
				} 
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run(String filename) throws FileNotFoundException, Exception {
		final Engine<DoubleGene, Double> engine = Engine
			.builder(
				CW1_GA::fitness,
				// Codec for 'x' vector.
				Codecs.ofVector(DoubleRange.of(-R, R), N))
			.populationSize(popSize)
			.optimize(Optimize.MINIMUM)
			.survivorsSize(numSurvivors)
			.survivorsSelector(new EliteSelector<>(numSurvivors))
			.offspringSelector(new TournamentSelector<>(tournamentSize))
			.alterers(
				new Mutator<>(probMutation), //default off
				//new GaussianMutator<>(probMutation), //default on
				//new UniformCrossover<>(probCrossover)) //default off
				new SinglePointCrossover<>(probCrossover)) //default on 
				//new MeanAlterer<>(probCrossover)) //default off
			.build();

		final EvolutionStatistics<Double, ?>
			statistics = EvolutionStatistics.ofNumber();
		
		
		
			FileWriter myWriter = new FileWriter(filename, true); //append to the file 
			final Phenotype<DoubleGene, Double> best = engine.stream()
					.limit(numIters)
					.peek(statistics)
					// Uncomment the following line to get updates at each iteration
					//.peek(r -> System.out.println(statistics))
					//.peek(r -> System.out.println("Generation : "+r.getGeneration()+", average fitness : "+((DoubleMomentStatistics)statistics.getFitness()).getMean()))
					
					//.peek(r -> System.out.println(r.getGeneration()+" "+((DoubleMomentStatistics)statistics.getFitness()).getMean()))
					.peek(r -> {
						try {
							myWriter.write(r.getGeneration()+" "+r.getBestFitness()+" "+((DoubleMomentStatistics)statistics.getFitness()).getMean()+" "+r.getWorstFitness()+"\n"); //get best fitness, avg and worse			
						} catch (IOException e) {
							System.out.println("An error occurred.");
							e.printStackTrace();
						}
					})
					.collect(toBestPhenotype());
			
			myWriter.close();
	
		//System.out.println(statistics);
		//System.out.println(best.getFitness());
		
	}



	//If a file is specified, parameters will be imported from it 
	public static void main(final String[] args) throws Exception {
		CW1_GA alg = new CW1_GA();
		
		/*
		if(args.length>0) {
			alg.parseParams(args[0]);
		}*/
		
		
		
		/*
		//for popsizenumiters folder
		String currentDirectory = "GA Scenario 2/popsizenumiters";
		
		System.out.println(String.format("%20s | %10s", "Population size", "Average time taken"));
		for (int fileno = 0; fileno < 5; fileno++) {
			alg.parseParams(String.format("GA Scenario 2/popsizenumiters/%d.config", fileno));
			
			//System.out.println("File loaded successfully. Starting...");
			
		
			
			long totaltime = 0;
			for (int i = 0; i < 30; i++) {
				long t1=System.currentTimeMillis();
				alg.run(String.format("%s/%d.txt",currentDirectory,fileno));
				long t2=System.currentTimeMillis();
				totaltime = (long) (totaltime + ((t2-t1)));
			}
			
			//System.out.println("Finished running algorithm from input config file");;
			System.out.println(String.format("%20s | %10s", popSize, (totaltime/1000.0)/30));
			
		}
		*/
		

		
		//for whole number tests 
		
		
		//String currentScenario = "GA Scenario 1/";
		String currentScenario = "GA Scenario 2/";
		
		//String currentParamChanged = "numsurvivors";
		//String currentParamChanged = "tournamentsize";
		//String currentParamChanged = "probmutationgaussian";
		String currentParamChanged = "probmutationnormal";
		//String currentParamChanged = "probsinglepointcrossover";
		//String currentParamChanged = "probmeanalterer";

		


		
		long totaltime = 0;
		
		System.out.println(String.format("%20s | %10s", currentParamChanged, "Average time taken (s)"));
		for (int fileno = 1; fileno < 11; fileno++) {
			alg.parseParams(String.format("%s/%d.config", currentScenario+currentParamChanged,fileno));
			
			//For int type params
			//int paramBeingChanged = tournamentSize;
			
			//For double type params
			double paramBeingChanged = probMutation;
			
			
			for (int i = 0; i < 30; i++) {
				long t1=System.currentTimeMillis();
		
				//alg.run(String.format("%s/%d.txt",currentScenario+currentParamChanged,paramBeingChanged)); //for whole numbers
				//alg.run(String.format("%s/%.3f.txt",currentScenario+currentParamChanged,paramBeingChanged)); // for 0.001-0.10 mutation probability
				alg.run(String.format("%s/%.2f.txt",currentScenario+currentParamChanged,paramBeingChanged)); // for 0.01-0.10 	
				//alg.run(String.format("%s/%.2f.txt",currentScenario+currentParamChanged,paramBeingChanged)); // for 0.1-1.0

				long t2=System.currentTimeMillis();
				totaltime = (long) (totaltime + ((t2-t1)));
			}

			System.out.println(String.format("%20s | %10f", paramBeingChanged, ((totaltime/1000.0)/30)));

		
		}
		
		System.out.println(String.format("\n %25s %10f", "Total runtime (s):", totaltime/1000.0));
		
		
		
		/*
		
		
		/*
		//alg.parseParams("GA Scenario 2/optimisedparams/2.config");

		//System.out.println("File loaded successfully. Starting...");
		
		
		long totaltime = 0;
		for (int i = 0; i < 30; i++) {
			long t1=System.currentTimeMillis();
			alg.run("GA Scenario 2/optimisedparams/2.txt");
			long t2=System.currentTimeMillis();
			totaltime = (long) (totaltime + ((t2-t1)));
		}
		
		*/
		
		
		//System.out.println(String.format("%20s | %10f", probCrossover, ((totaltime/1000.0)/30)));	
		
		/*
		//for multiple population sizes and iterations against other parameters
		
				String currentDirectory = "probmutation x popsize";
				for (int fileno = 1; fileno < 11; fileno++) {
					System.out.println("Config file no is " +fileno);

					alg.parseParams(String.format("%s/%d.config", currentDirectory,fileno));

					System.out.println("File loaded successfully. Starting...");
					
					
					for (int i = 0; i<5; i++) {
				
						System.out.println("Pop size "+popSize);
						System.out.println("Iteration size "+numIters);
						
						//Run that popsize and iterations 30 times
						for (int repeat = 0; repeat < 30; repeat++) {
							alg.run(String.format("%s/0.%d.txt",currentDirectory,fileno));
						}
						
						popSize = popSize / 10;
						numIters = numIters * 10;
						
					}
					
					System.out.println("Done!");

			

					System.out.println("Finished running algorithm from input config file");
				}
				
				*/
		
		
		
	}
}
