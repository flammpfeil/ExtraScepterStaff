package mods.flammpfeil.extrascepterstaff.common;

import cpw.mods.fml.common.SidedProxy;

public class InitProxy {
	@SidedProxy(clientSide = "mods.flammpfeil.extrascepterstaff.client.InitProxyClient", serverSide = "mods.flammpfeil.extrascepterstaff.common.InitProxy")
	public static InitProxy proxy;

	public void initializeItemRenderer() {}

}
