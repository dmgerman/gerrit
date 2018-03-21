begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
name|common
operator|.
name|Nullable
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
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_comment
comment|/** Provides {@link GerritInstanceName} from {@code gerrit.name}. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|GerritInstanceNameProvider
specifier|public
class|class
name|GerritInstanceNameProvider
implements|implements
name|Provider
argument_list|<
name|String
argument_list|>
block|{
DECL|field|instanceName
specifier|private
specifier|final
name|String
name|instanceName
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritInstanceNameProvider ( @erritServerConfig Config config, @CanonicalWebUrl @Nullable Provider<String> canonicalUrlProvider)
specifier|public
name|GerritInstanceNameProvider
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
annotation|@
name|CanonicalWebUrl
annotation|@
name|Nullable
name|Provider
argument_list|<
name|String
argument_list|>
name|canonicalUrlProvider
parameter_list|)
block|{
name|this
operator|.
name|instanceName
operator|=
name|getInstanceName
argument_list|(
name|config
argument_list|,
name|canonicalUrlProvider
argument_list|)
expr_stmt|;
block|}
DECL|method|getInstanceName (Config config, @Nullable Provider<String> canonicalUrlProvider)
specifier|private
name|String
name|getInstanceName
parameter_list|(
name|Config
name|config
parameter_list|,
annotation|@
name|Nullable
name|Provider
argument_list|<
name|String
argument_list|>
name|canonicalUrlProvider
parameter_list|)
block|{
name|String
name|instanceName
init|=
name|config
operator|.
name|getString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"instanceName"
argument_list|)
decl_stmt|;
if|if
condition|(
name|instanceName
operator|!=
literal|null
operator|||
name|canonicalUrlProvider
operator|==
literal|null
condition|)
block|{
return|return
name|instanceName
return|;
block|}
return|return
name|canonicalUrlProvider
operator|.
name|get
argument_list|()
return|;
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
name|instanceName
return|;
block|}
block|}
end_class

end_unit

