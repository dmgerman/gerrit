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

begin_comment
comment|/** Loadable module type for the different Gerrit injectors. */
end_comment

begin_enum
DECL|enum|LibModuleType
specifier|public
enum|enum
name|LibModuleType
block|{
comment|/** Module for the sysInjector. */
DECL|enumConstant|SYS_MODULE
name|SYS_MODULE
argument_list|(
literal|"Module"
argument_list|)
block|,
comment|/** Module for the dbInjector. */
DECL|enumConstant|DB_MODULE
name|DB_MODULE
argument_list|(
literal|"DbModule"
argument_list|)
block|;
DECL|field|configKey
specifier|private
specifier|final
name|String
name|configKey
decl_stmt|;
DECL|method|LibModuleType (String configKey)
name|LibModuleType
parameter_list|(
name|String
name|configKey
parameter_list|)
block|{
name|this
operator|.
name|configKey
operator|=
name|configKey
expr_stmt|;
block|}
comment|/**    * Returns the module type for libModule loaded from<gerrit_site/lib> directory.    *    * @return module type string    */
DECL|method|getConfigKey ()
specifier|public
name|String
name|getConfigKey
parameter_list|()
block|{
return|return
name|configKey
return|;
block|}
block|}
end_enum

end_unit

