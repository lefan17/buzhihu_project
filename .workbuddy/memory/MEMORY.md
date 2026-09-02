# 项目长期笔记 · 不止乎（buzhihu）

## 仓库与版本控制现状（2026-09-02 核实）

- **远端**：`lefan17/buzhihu_project`（阿帆自己的仓库，非 fork）。2025-02-20 创建，`pushed_at` 停在创建当天，`size=0`，GitHub API 返回 **"Git Repository is empty"** —— 完全空仓库，无 README、无 description、无 topics、无 license。
- **本地（2026-09-02 已解决）**：已 `git init -b main`，分支 main，完成首次提交 `c521622`（161 个文件），远端 origin 已指向 `https://github.com/lefan17/buzhihu_project.git`。**尚未 push**，等阿帆授权。
- **git 身份**：用**项目级**配置（`git config` 不带 `--global`），name=`lefan17`，email=`128964809+lefan17@users.noreply.github.com`（GitHub 隐私邮箱，能关联账号且不暴露真邮箱）。**刻意不动全局配置**，因为阿帆另有 agent-test-hub、software-testing-practice 两个仓库，明确要求不要波及。
- **git 环境**：git 2.55.0；全局 `user.name`/`user.email` 仍未设置（保持原样）；`credential.helper` 未设置，push 时需 GCM 弹窗授权一次。
- **仓库体积**：清理后 `.git` 为 8.0MB（161 文件）。首次 add 时误纳了整个 70MB `files/`，`git reset` 后对象仍悬空占 39MB，需 `git gc --prune=now` 才真正释放。

## GitHub 访问通道（两条相互独立）

| 通道 | 状态 | 说明 |
|---|---|---|
| WorkBuddy GitHub connector | **已认证** | 账号 `lefan17`（Lefan Ma，西亚斯）。可直接用 `mcp__github__*` 工具读写仓库 |
| 本机 `gh` CLI | **未登录** | gh 2.98.0 已装，`%APPDATA%\GitHub CLI\hosts.yml` 留有 `lefan17` 用户名但 token 已失效 |

→ 我操作阿帆的 GitHub 走 connector 即可，**不需要**他去登录 gh CLI。但本机 `git push` 仍需凭证（GCM 弹窗授权一次）。

## 阿帆的仓库风格参照

他另有两个仓库规范度明显更高，可作为 buzhihu 改造的达标线：
- `agent-test-hub`（2026-08，Python，LangGraph + LLM 测试编排）：有 description、9 个 topics、MIT license、GitHub Pages 站点
- `software-testing-practice`（2026-09，Flask + pytest 契约测试）：有 description、topics、MIT license

→ buzhihu_project 是早期遗留（2025-02），规范度远低于他当前水平。**改造门面时应以 agent-test-hub 为标准**，而非"能跑就行"。

## 项目核心结论（详见 `项目体检报告.md`）

项目基于 SpringBoot2+Vue2 后台管理脚手架二次开发（痕迹：`xm-blog` 库名、`xm-user` localStorage 键、`admin@xm.com`、title「管理系统」）。三个致命缺陷的根子是**脚手架基因 vs 内容社区需求冲突**（访问模型 / 权限粒度 / 内容生产 / 输入可信度 四个维度全对立），不是编码失误。

改造顺序：定位二选一 → 去脚手架化 → 差异化功能 → 工程门面。
