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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|config
operator|.
name|GerritServerConfig
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
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|UserConfig
import|;
end_import

begin_comment
comment|/** Provides {@link PersonIdent} annotated with {@link GerritPersonIdent}. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|GerritPersonIdentProvider
specifier|public
class|class
name|GerritPersonIdentProvider
implements|implements
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|email
specifier|private
specifier|final
name|String
name|email
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritPersonIdentProvider (@erritServerConfig final Config cfg)
specifier|public
name|GerritPersonIdentProvider
parameter_list|(
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|cfg
parameter_list|)
block|{
name|String
name|name
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"user"
argument_list|,
literal|null
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
literal|"Gerrit Code Review"
expr_stmt|;
block|}
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|email
operator|=
name|cfg
operator|.
name|get
argument_list|(
name|UserConfig
operator|.
name|KEY
argument_list|)
operator|.
name|getCommitterEmail
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|PersonIdent
name|get
parameter_list|()
block|{
return|return
operator|new
name|PersonIdent
argument_list|(
name|name
argument_list|,
name|email
argument_list|)
return|;
block|}
block|}
end_class

end_unit

