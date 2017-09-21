begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|securestore
operator|.
name|SecureStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_class
DECL|class|GerritConfig
class|class
name|GerritConfig
extends|extends
name|Config
block|{
DECL|field|gerritConfig
specifier|private
specifier|final
name|Config
name|gerritConfig
decl_stmt|;
DECL|field|secureStore
specifier|private
specifier|final
name|SecureStore
name|secureStore
decl_stmt|;
DECL|method|GerritConfig (Config noteDbConfigOverGerritConfig, Config gerritConfig, SecureStore secureStore)
name|GerritConfig
parameter_list|(
name|Config
name|noteDbConfigOverGerritConfig
parameter_list|,
name|Config
name|gerritConfig
parameter_list|,
name|SecureStore
name|secureStore
parameter_list|)
block|{
name|super
argument_list|(
name|noteDbConfigOverGerritConfig
argument_list|)
expr_stmt|;
name|this
operator|.
name|gerritConfig
operator|=
name|gerritConfig
expr_stmt|;
name|this
operator|.
name|secureStore
operator|=
name|secureStore
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getString (String section, String subsection, String name)
specifier|public
name|String
name|getString
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|String
name|secure
init|=
name|secureStore
operator|.
name|get
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|secure
operator|!=
literal|null
condition|)
block|{
return|return
name|secure
return|;
block|}
return|return
name|super
operator|.
name|getString
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getStringList (String section, String subsection, String name)
specifier|public
name|String
index|[]
name|getStringList
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|String
index|[]
name|secure
init|=
name|secureStore
operator|.
name|getList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|secure
operator|!=
literal|null
operator|&&
name|secure
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|secure
return|;
block|}
return|return
name|super
operator|.
name|getStringList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toText ()
specifier|public
name|String
name|toText
parameter_list|()
block|{
comment|// Only show the contents of gerrit.config, hiding the implementation detail that some values
comment|// may come from secure.config (or another secure store) and notedb.config.
return|return
name|gerritConfig
operator|.
name|toText
argument_list|()
return|;
block|}
block|}
end_class

end_unit

