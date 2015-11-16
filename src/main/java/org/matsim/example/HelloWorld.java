/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package org.matsim.example;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.otfvis.OTFVis;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.mobsim.framework.Mobsim;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.QSimUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.trafficmonitoring.FreeSpeedTravelTime;
import org.matsim.vis.otfvis.OnTheFlyServer;

import com.google.inject.Provider;

/**
 * @author nagel
 *
 */
public class HelloWorld {

	public static void main(String[] args) {
		
		// This creates a default matsim config:
		final Config config = ConfigUtils.createConfig();
		
		config.controler().setLastIteration(1);
		config.controler().setOverwriteFileSetting( OverwriteFileSetting.deleteDirectoryIfExists );

		// This creates a default matsim scenario (which is empty):
		final Scenario scenario = ScenarioUtils.createScenario(config) ;

		final Controler controler = new Controler( scenario ) ;
		controler.addOverridingModule(new AbstractModule() {
			
			@Override
			public void install() {
//				bindTravelDisutilityFactory().toInstance(new FreeSpeedTravelTime());
				this.bindMobsim().toProvider(new Provider<Mobsim>(){

					@Override
					public Mobsim get() {
						QSim qsim = QSimUtils.createDefaultQSim(scenario, controler.getEvents());
						OnTheFlyServer server = OTFVis.startServerAndRegisterWithQSim(config,scenario,controler.getEvents(),qsim);
						return qsim;
					}
							
				} );
			}
		});
		
		// This indeed runs iterations, but based on an empty scenario:
		controler.run();

	}

}
