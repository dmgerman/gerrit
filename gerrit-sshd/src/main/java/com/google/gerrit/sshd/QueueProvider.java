begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|com.google.gerrit.sshd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
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
name|git
operator|.
name|WorkQueue
import|;
end_import

begin_interface
DECL|interface|QueueProvider
specifier|public
interface|interface
name|QueueProvider
block|{
DECL|method|getInteractiveQueue ()
specifier|public
name|WorkQueue
operator|.
name|Executor
name|getInteractiveQueue
parameter_list|()
function_decl|;
DECL|method|getBatchQueue ()
specifier|public
name|WorkQueue
operator|.
name|Executor
name|getBatchQueue
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

