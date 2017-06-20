begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.testutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Enums
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

begin_comment
comment|/**  * Whether to enable/disable tests using SSH by inspecting the global environment.  *  *<p>Acceptance tests should generally not inspect this directly, since SSH may also be disabled on  * a per-class or per-method basis. Inject {@code @SshEnabled boolean} instead.  */
end_comment

begin_enum
DECL|enum|SshMode
specifier|public
enum|enum
name|SshMode
block|{
comment|/** Tests annotated with UseSsh will be disabled. */
DECL|enumConstant|NO
name|NO
block|,
comment|/** Tests annotated with UseSsh will be enabled. */
DECL|enumConstant|YES
name|YES
block|;
DECL|field|ENV_VAR
specifier|private
specifier|static
specifier|final
name|String
name|ENV_VAR
init|=
literal|"GERRIT_USE_SSH"
decl_stmt|;
DECL|field|SYS_PROP
specifier|private
specifier|static
specifier|final
name|String
name|SYS_PROP
init|=
literal|"gerrit.use.ssh"
decl_stmt|;
DECL|method|get ()
specifier|public
specifier|static
name|SshMode
name|get
parameter_list|()
block|{
name|String
name|value
init|=
name|System
operator|.
name|getenv
argument_list|(
name|ENV_VAR
argument_list|)
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|value
operator|=
name|System
operator|.
name|getProperty
argument_list|(
name|SYS_PROP
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|YES
return|;
block|}
name|value
operator|=
name|value
operator|.
name|toUpperCase
argument_list|()
expr_stmt|;
name|SshMode
name|mode
init|=
name|Enums
operator|.
name|getIfPresent
argument_list|(
name|SshMode
operator|.
name|class
argument_list|,
name|value
argument_list|)
operator|.
name|orNull
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|System
operator|.
name|getenv
argument_list|(
name|ENV_VAR
argument_list|)
argument_list|)
condition|)
block|{
name|checkArgument
argument_list|(
name|mode
operator|!=
literal|null
argument_list|,
literal|"Invalid value for env variable %s: %s"
argument_list|,
name|ENV_VAR
argument_list|,
name|System
operator|.
name|getenv
argument_list|(
name|ENV_VAR
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|checkArgument
argument_list|(
name|mode
operator|!=
literal|null
argument_list|,
literal|"Invalid value for system property %s: %s"
argument_list|,
name|SYS_PROP
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
name|SYS_PROP
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|mode
return|;
block|}
DECL|method|useSsh ()
specifier|public
specifier|static
name|boolean
name|useSsh
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|==
name|YES
return|;
block|}
block|}
end_enum

end_unit

