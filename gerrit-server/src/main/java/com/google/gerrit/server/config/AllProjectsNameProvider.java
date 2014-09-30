begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|Provider
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
DECL|class|AllProjectsNameProvider
specifier|public
class|class
name|AllProjectsNameProvider
implements|implements
name|Provider
argument_list|<
name|AllProjectsName
argument_list|>
block|{
DECL|field|DEFAULT
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT
init|=
literal|"All-Projects"
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|AllProjectsName
name|name
decl_stmt|;
annotation|@
name|Inject
DECL|method|AllProjectsNameProvider (@erritServerConfig Config cfg)
name|AllProjectsNameProvider
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|String
name|n
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"allProjects"
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|==
literal|null
operator|||
name|n
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|n
operator|=
name|DEFAULT
expr_stmt|;
block|}
name|name
operator|=
operator|new
name|AllProjectsName
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
DECL|method|get ()
specifier|public
name|AllProjectsName
name|get
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

