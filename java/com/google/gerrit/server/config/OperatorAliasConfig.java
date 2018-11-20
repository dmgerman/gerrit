begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
annotation|@
name|Singleton
DECL|class|OperatorAliasConfig
specifier|public
class|class
name|OperatorAliasConfig
block|{
DECL|field|SECTION
specifier|private
specifier|static
specifier|final
name|String
name|SECTION
init|=
literal|"operator-alias"
decl_stmt|;
DECL|field|SUBSECTION_CHANGE
specifier|private
specifier|static
specifier|final
name|String
name|SUBSECTION_CHANGE
init|=
literal|"change"
decl_stmt|;
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
DECL|field|changeQueryOperatorAliases
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|changeQueryOperatorAliases
decl_stmt|;
annotation|@
name|Inject
DECL|method|OperatorAliasConfig (@erritServerConfig Config cfg)
name|OperatorAliasConfig
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
name|changeQueryOperatorAliases
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|loadChangeOperatorAliases
argument_list|()
expr_stmt|;
block|}
DECL|method|getChangeQueryOperatorAliases ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getChangeQueryOperatorAliases
parameter_list|()
block|{
return|return
name|changeQueryOperatorAliases
return|;
block|}
DECL|method|loadChangeOperatorAliases ()
specifier|private
name|void
name|loadChangeOperatorAliases
parameter_list|()
block|{
for|for
control|(
name|String
name|name
range|:
name|cfg
operator|.
name|getNames
argument_list|(
name|SECTION
argument_list|,
name|SUBSECTION_CHANGE
argument_list|)
control|)
block|{
name|changeQueryOperatorAliases
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|cfg
operator|.
name|getString
argument_list|(
name|SECTION
argument_list|,
name|SUBSECTION_CHANGE
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

