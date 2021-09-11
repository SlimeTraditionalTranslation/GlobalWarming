# 全球暖化
黏液科技的附加 繁體翻譯版<br>

此Slimefun附加旨在為遊戲添加氣候變化機制.
一個值得嘗試, 99%可配置的插件給Minecraft伺服器.

> 此為**非官方**版本, 請勿在該作者問題追蹤內回報! <br>
> [原作連結](https://github.com/poma123/GlobalWarming) | [非官方Discord](https://discord.gg/GF4CwjFXT9)

## 導航
* [下載](#下載)
* [配置](#配置)
* [機制](#機制)
  * [環境機制](#環境機制)
  * [汙染機器](#汙染機器)
  * [新聞快報系統](#新聞快報系統)
* [物品與機器](#物品與機器)
* [API](#api)

## 下載
| 非官方繁體中文版 | 官方英文版 |
| -------- | -------- |
| 點下方圖片下載 | 點下方圖片下載 |
| [![Build Status](https://xMikux.github.io/builds/SlimeTraditionalTranslation/GlobalWarming/master/badge.svg)](https://xMikux.github.io/builds/SlimeTraditionalTranslation/GlobalWarming/master) | [![Build Status](https://thebusybiscuit.github.io/builds/poma123/GlobalWarming/master/badge.svg)](https://thebusybiscuit.github.io/builds/poma123/GlobalWarming/master) |

## 配置
成功安裝此附加後,請查看[config.yml](https://github.com/xMikux/GlobalWarming/tree/master/src/main/resources/config.yml).
- `worlds` 可讓你將世界列入白名單或排除在氣候變化機制外
- `world-filter-type` 定義世界過濾器要如何運行 (可用類型: ``blacklist``, ``whitelist``)
- 在`mechanics`部分下,你可以自定義可用的氣候變化機制
- 在`pollution`部分下包含汙染產生與污染吸收,與物品和實體
- 在`temperature-options`部分下,你可以配置如何根據汙染和天氣來計算溫度.

在你完成編輯檔案之後, 重啟你的伺服器!
## 機制
### 環境機制:

- 森林大火 (在已加載的區塊內發生,大火會隨機發生在最高溫度的方塊上)
- 冰溶化 (在已加載的區塊內發生,冰會在高溫下隨機融化)
- 玩家緩速 (如果溫度足夠高或低,就會發生此情況)
- 玩家燃燒 (如果溫度過高,就會發生)

### 汙染機器
汙染可以在每個世界範圍內變化,有兩種汙染機制:

##### 1. 汙染產生
- 動物繁殖時
- 在受汙染的Slimefun機器完成它們的處理.
- 在受汙染的Slimefun物品被使用在Slimefun的機器.

##### 2. 汙染吸收
- 當樹長大
- 當Slimefun機器吸收完之後. (默認: 空氣壓縮機)

### 新聞快報系統:
- 每當世界上污染產生變化時,所在該世界的玩家會收到一條"新聞快報"的消息,並從現實世界中隨機選擇一個不錯的新聞快報.

## 物品與機器
- 溫度計 (顯示當前溫度)
- 空氣質量儀 (顯示當前升高溫度)
- 空氣壓縮機 (將二氧化碳壓縮到空罐中並且吸收汙染)
- 空罐
- 二氧化碳罐 (包括壓縮二氧化碳)
- 硃砂石 (GEO資源, 需要用於汞)
- 汞 (資源,需要用於製作空氣壓縮機)
- 濾網 (需要用於空氣壓縮機)

![image](https://user-images.githubusercontent.com/25465545/96293130-90bcfa80-0fea-11eb-9f16-d57105148973.png)
## API
All well-documented API classes can be found under the [`me.poma123.globalwarming.api`](https://github.com/poma123/GlobalWarming/tree/master/src/main/java/me/poma123/globalwarming/api) package.
