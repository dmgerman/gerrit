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
name|gerrit
operator|.
name|server
operator|.
name|account
operator|.
name|EmailExpander
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
DECL|class|EmailExpanderProvider
class|class
name|EmailExpanderProvider
implements|implements
name|Provider
argument_list|<
name|EmailExpander
argument_list|>
block|{
DECL|field|expander
specifier|private
specifier|final
name|EmailExpander
name|expander
decl_stmt|;
annotation|@
name|Inject
DECL|method|EmailExpanderProvider (@erritServerConfig final Config cfg)
name|EmailExpanderProvider
parameter_list|(
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|cfg
parameter_list|)
block|{
specifier|final
name|String
name|s
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"auth"
argument_list|,
literal|null
argument_list|,
literal|"emailformat"
argument_list|)
decl_stmt|;
if|if
condition|(
name|EmailExpander
operator|.
name|Simple
operator|.
name|canHandle
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|expander
operator|=
operator|new
name|EmailExpander
operator|.
name|Simple
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|EmailExpander
operator|.
name|None
operator|.
name|canHandle
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|expander
operator|=
name|EmailExpander
operator|.
name|None
operator|.
name|INSTANCE
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid auth.emailformat: "
operator|+
name|s
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|EmailExpander
name|get
parameter_list|()
block|{
return|return
name|expander
return|;
block|}
block|}
end_class

end_unit

