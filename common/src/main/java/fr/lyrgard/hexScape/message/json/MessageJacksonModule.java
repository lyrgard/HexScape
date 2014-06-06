package fr.lyrgard.hexScape.message.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import fr.lyrgard.hexScape.message.AbstractMessage;

public class MessageJacksonModule extends SimpleModule{

	private static final long serialVersionUID = -7975913175288737218L;
	
	public MessageJacksonModule() {
		super("MessageJacksonModule", new Version(0,0,1,null, null, null));
	}
	@Override
	public void setupModule(SetupContext context)
	{
		context.setMixInAnnotations(AbstractMessage.class, PolymorphicMessageMixIn.class);
	}

}
