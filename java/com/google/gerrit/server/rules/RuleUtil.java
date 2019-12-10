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
DECL|package|com.google.gerrit.server.rules
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|rules
package|;
end_package

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
comment|/** Provides utility methods for configuring and running Prolog rules inside Gerrit. */
end_comment

begin_class
DECL|class|RuleUtil
specifier|public
class|class
name|RuleUtil
block|{
comment|/**    * Returns the reduction limit to be applied to the Prolog machine to prevent infinite loops and    * other forms of computational overflow.    */
DECL|method|reductionLimit (Config gerritConfig)
specifier|public
specifier|static
name|int
name|reductionLimit
parameter_list|(
name|Config
name|gerritConfig
parameter_list|)
block|{
name|int
name|limit
init|=
name|gerritConfig
operator|.
name|getInt
argument_list|(
literal|"rules"
argument_list|,
literal|null
argument_list|,
literal|"reductionLimit"
argument_list|,
literal|100000
argument_list|)
decl_stmt|;
return|return
name|limit
operator|<=
literal|0
condition|?
name|Integer
operator|.
name|MAX_VALUE
else|:
name|limit
return|;
block|}
comment|/**    * Returns the compile reduction limit to be applied to the Prolog machine to prevent infinite    * loops and other forms of computational overflow. The compiled reduction limit should be used    * when user-provided Prolog code is compiled by the interpreter before the limit gets applied.    */
DECL|method|compileReductionLimit (Config gerritConfig)
specifier|public
specifier|static
name|int
name|compileReductionLimit
parameter_list|(
name|Config
name|gerritConfig
parameter_list|)
block|{
name|int
name|limit
init|=
name|gerritConfig
operator|.
name|getInt
argument_list|(
literal|"rules"
argument_list|,
literal|null
argument_list|,
literal|"compileReductionLimit"
argument_list|,
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
literal|10L
operator|*
name|reductionLimit
argument_list|(
name|gerritConfig
argument_list|)
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|limit
operator|<=
literal|0
condition|?
name|Integer
operator|.
name|MAX_VALUE
else|:
name|limit
return|;
block|}
block|}
end_class

end_unit

