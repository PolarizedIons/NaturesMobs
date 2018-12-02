package net.polarizedions.naturesmobs.registry;

public interface IModContent {
    String getBaseName();

    default boolean addToCreative() {
        return true;
    }

    @SuppressWarnings("unchecked")
    default <T extends IModContent> T register() {
        ModRegistry.add(this);
        return (T) this;
    }
}
