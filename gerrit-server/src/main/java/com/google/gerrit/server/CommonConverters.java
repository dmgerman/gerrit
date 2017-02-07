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
name|extensions
operator|.
name|common
operator|.
name|GitPerson
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
name|PersonIdent
import|;
end_import

begin_comment
comment|/**  * Converters to classes in {@code com.google.gerrit.extensions.common}.  *  *<p>The server frequently needs to convert internal types to types exposed in the extension API,  * but the converters themselves are not part of this API. This class contains such converters as  * static utility methods.  */
end_comment

begin_class
DECL|class|CommonConverters
specifier|public
class|class
name|CommonConverters
block|{
DECL|method|toGitPerson (PersonIdent ident)
specifier|public
specifier|static
name|GitPerson
name|toGitPerson
parameter_list|(
name|PersonIdent
name|ident
parameter_list|)
block|{
name|GitPerson
name|result
init|=
operator|new
name|GitPerson
argument_list|()
decl_stmt|;
name|result
operator|.
name|name
operator|=
name|ident
operator|.
name|getName
argument_list|()
expr_stmt|;
name|result
operator|.
name|email
operator|=
name|ident
operator|.
name|getEmailAddress
argument_list|()
expr_stmt|;
name|result
operator|.
name|date
operator|=
operator|new
name|Timestamp
argument_list|(
name|ident
operator|.
name|getWhen
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|tz
operator|=
name|ident
operator|.
name|getTimeZoneOffset
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|CommonConverters ()
specifier|private
name|CommonConverters
parameter_list|()
block|{}
block|}
end_class

end_unit

