package cn.chengzhiya.mhdftools.hook.impl;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.util.BigDecimalUtil;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.database.EconomyDataUtil;
import cn.chengzhiya.mhdftools.util.feature.EconomyUtil;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;

import java.util.List;

public final class EconomyImpl extends AbstractEconomy {
    public EconomyImpl() {
        Bukkit.getServicesManager().register(Economy.class, this, Main.instance, ServicePriority.Normal);
    }

    public void unhook() {
        Bukkit.getServicesManager().unregister(Economy.class, this);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "MHDF-Tools";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        return String.format("%.2f", amount);
    }

    @Override
    public String currencyNamePlural() {
        return this.currencyNameSingular();
    }

    public String currencyNameSingular() {
        return EconomyUtil.getMoneyName();
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return EconomyDataUtil.ifEconomyDataExist(player);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String world) {
        return this.hasAccount(player);
    }

    @Override
    public boolean hasAccount(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return this.hasAccount(player);
    }

    @Override
    public boolean hasAccount(String name, String world) {
        return this.hasAccount(name);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return EconomyUtil.getMoney(player).doubleValue();
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return this.getBalance(player);
    }

    @Override
    public double getBalance(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return this.getBalance(player);
    }

    @Override
    public double getBalance(String name, String world) {
        return this.getBalance(name);
    }

    @Override
    public boolean has(String name, double amount) {
        return this.getBalance(name) >= amount;
    }

    @Override
    public boolean has(String name, String world, double amount) {
        return this.has(name, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        EconomyUtil.takeMoney(player, BigDecimalUtil.toBigDecimal(amount));
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String world, double amount) {
        return this.depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, double amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return this.withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, String world, double amount) {
        return this.withdrawPlayer(name, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        double tax = 0;
        if (ConfigUtil.getConfig().getBoolean("economySettings.personalIncomeTax.enable")) {
            tax = amount * ConfigUtil.getConfig().getDouble("economySettings.personalIncomeTax.rate");
            ActionUtil.sendMessage(player.getPlayer(), LangUtil.i18n("economy.tax")
                    .replace("{amount}", String.valueOf(tax))
            );
        }
        EconomyUtil.addMoney(player, BigDecimalUtil.toBigDecimal(amount - tax));
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {
        return this.depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String name, double amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return this.depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String name, String world, double amount) {
        return depositPlayer(name, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String world) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse isBankOwner(String name, String world) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse isBankMember(String name, String world) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        if (EconomyDataUtil.ifEconomyDataExist(player)) {
            return false;
        }

        EconomyDataUtil.initEconomyData(player);
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return this.createPlayerAccount(player);
    }

    @Override
    public boolean createPlayerAccount(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return this.createPlayerAccount(player);
    }

    @Override
    public boolean createPlayerAccount(String name, String world) {
        return this.createPlayerAccount(name);
    }
}
