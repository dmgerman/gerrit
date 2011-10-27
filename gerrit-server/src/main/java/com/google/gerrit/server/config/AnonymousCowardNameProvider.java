begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|class|AnonymousCowardNameProvider
specifier|public
class|class
name|AnonymousCowardNameProvider
implements|implements
name|Provider
argument_list|<
name|String
argument_list|>
block|{
DECL|field|anonymousCoward
specifier|private
specifier|final
name|String
name|anonymousCoward
decl_stmt|;
annotation|@
name|Inject
DECL|method|AnonymousCowardNameProvider (@erritServerConfig final Config cfg)
specifier|public
name|AnonymousCowardNameProvider
parameter_list|(
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|cfg
parameter_list|)
block|{
name|String
name|anonymousCoward
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"user"
argument_list|,
literal|null
argument_list|,
literal|"anonymousCoward"
argument_list|)
decl_stmt|;
if|if
condition|(
name|anonymousCoward
operator|==
literal|null
condition|)
block|{
name|anonymousCoward
operator|=
literal|"Anonymous Coward"
expr_stmt|;
block|}
name|this
operator|.
name|anonymousCoward
operator|=
name|anonymousCoward
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|anonymousCoward
return|;
block|}
block|}
end_class

end_unit

