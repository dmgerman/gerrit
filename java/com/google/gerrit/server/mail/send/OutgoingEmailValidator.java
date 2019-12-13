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
DECL|package|com.google.gerrit.server.mail.send
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
operator|.
name|send
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|validator
operator|.
name|routines
operator|.
name|DomainValidator
operator|.
name|ArrayType
operator|.
name|GENERIC_PLUS
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
name|flogger
operator|.
name|FluentLogger
import|;
end_import

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
name|Singleton
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|validator
operator|.
name|routines
operator|.
name|DomainValidator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|validator
operator|.
name|routines
operator|.
name|EmailValidator
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
comment|/**  * Validator that checks if an email address is valid and allowed for receiving notification emails.  *  *<p>An email address is valid if it is syntactically correct.  *  *<p>An email address is allowed if its top level domain is allowed by Gerrit's configuration.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|OutgoingEmailValidator
specifier|public
class|class
name|OutgoingEmailValidator
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
annotation|@
name|Inject
DECL|method|OutgoingEmailValidator (@erritServerConfig Config config)
name|OutgoingEmailValidator
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|)
block|{
name|String
index|[]
name|allowTLD
init|=
name|config
operator|.
name|getStringList
argument_list|(
literal|"sendemail"
argument_list|,
literal|null
argument_list|,
literal|"allowTLD"
argument_list|)
decl_stmt|;
if|if
condition|(
name|allowTLD
operator|.
name|length
operator|!=
literal|0
condition|)
block|{
try|try
block|{
name|DomainValidator
operator|.
name|updateTLDOverride
argument_list|(
name|GENERIC_PLUS
argument_list|,
name|allowTLD
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// Should only happen in tests, where the OutgoingEmailValidator
comment|// is instantiated repeatedly.
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"Failed to update TLD override: %s"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|isValid (String addr)
specifier|public
name|boolean
name|isValid
parameter_list|(
name|String
name|addr
parameter_list|)
block|{
return|return
name|EmailValidator
operator|.
name|getInstance
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
operator|.
name|isValid
argument_list|(
name|addr
argument_list|)
return|;
block|}
block|}
end_class

end_unit

