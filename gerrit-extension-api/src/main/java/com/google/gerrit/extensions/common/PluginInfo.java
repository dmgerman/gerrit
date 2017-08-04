begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|class|PluginInfo
specifier|public
class|class
name|PluginInfo
block|{
DECL|field|id
specifier|public
specifier|final
name|String
name|id
decl_stmt|;
DECL|field|version
specifier|public
specifier|final
name|String
name|version
decl_stmt|;
DECL|field|indexUrl
specifier|public
specifier|final
name|String
name|indexUrl
decl_stmt|;
DECL|field|filename
specifier|public
specifier|final
name|String
name|filename
decl_stmt|;
DECL|field|disabled
specifier|public
specifier|final
name|Boolean
name|disabled
decl_stmt|;
DECL|method|PluginInfo (String id, String version, String indexUrl, String filename, Boolean disabled)
specifier|public
name|PluginInfo
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|indexUrl
parameter_list|,
name|String
name|filename
parameter_list|,
name|Boolean
name|disabled
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|this
operator|.
name|indexUrl
operator|=
name|indexUrl
expr_stmt|;
name|this
operator|.
name|filename
operator|=
name|filename
expr_stmt|;
name|this
operator|.
name|disabled
operator|=
name|disabled
expr_stmt|;
block|}
block|}
end_class

end_unit

