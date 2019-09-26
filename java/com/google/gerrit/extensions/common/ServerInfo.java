begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
package|;
end_package

begin_class
DECL|class|ServerInfo
specifier|public
class|class
name|ServerInfo
block|{
DECL|field|accounts
specifier|public
name|AccountsInfo
name|accounts
decl_stmt|;
DECL|field|auth
specifier|public
name|AuthInfo
name|auth
decl_stmt|;
DECL|field|change
specifier|public
name|ChangeConfigInfo
name|change
decl_stmt|;
DECL|field|download
specifier|public
name|DownloadInfo
name|download
decl_stmt|;
DECL|field|gerrit
specifier|public
name|GerritInfo
name|gerrit
decl_stmt|;
DECL|field|noteDbEnabled
specifier|public
name|Boolean
name|noteDbEnabled
decl_stmt|;
DECL|field|plugin
specifier|public
name|PluginConfigInfo
name|plugin
decl_stmt|;
DECL|field|sshd
specifier|public
name|SshdInfo
name|sshd
decl_stmt|;
DECL|field|suggest
specifier|public
name|SuggestInfo
name|suggest
decl_stmt|;
DECL|field|user
specifier|public
name|UserConfigInfo
name|user
decl_stmt|;
DECL|field|receive
specifier|public
name|ReceiveInfo
name|receive
decl_stmt|;
DECL|field|defaultTheme
specifier|public
name|String
name|defaultTheme
decl_stmt|;
block|}
end_class

end_unit

